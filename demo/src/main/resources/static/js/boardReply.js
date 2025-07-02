let localeClass = "";
let emoticonMap = {};

window.onload = function() {
	localeClass = $("#localeClass").text().trim();
	loadEmoticons().then(() => getBoardReply(board_id));

	document.getElementById("reply_insert_form").setAttribute("data-placeholder", getMsg("msg_reply_placeholder"));

	const session_user_id = $("#session_user_id").text().trim();
	if (!session_user_id) {
		$("#reply_insert_form").attr("contenteditable", false).css("cursor", "not-allowed").css("opacity", 0.5);
		$("#reply_insert_btn").prop("disabled", true).css("cursor", "not-allowed").css("opacity", 0.5);
		$("#openEmoticonModalBtn").prop("disabled", true).css("cursor", "not-allowed").css("opacity", 0.5);
		$("#reply_login_msg").show();
	}
};

function getMsg(id) {
	return document.getElementById(id)?.textContent?.trim() || '';
}

function loadEmoticons() {
	return new Promise((resolve) => {
		$.get("/emoticon/approvedList", function(list) {
			emoticonMap = {};
			list.forEach(e => {
				emoticonMap[e.emoticon_name] = e.emoticon_file_name;
			});
			resolve();
		});
	});
}

function renderEmoticonsIn(elem) {
	const contentEl = $(elem);
	let html = contentEl.html();
	if (!html) return;

	html = html.replace(/<emoticon name="([^"]+)"><\/emoticon>/g, function(match, name) {
		const fileName = emoticonMap[name];
		if (!fileName) return name;
		return `<img src="/uploads/emoticon/${fileName}" alt="${name}" class="reply-emoticon">`;
	});
	contentEl.html(html);
}

function renderInputEmoticons() {
	renderEmoticonsIn("#reply_insert_form");
	placeCaretAtEnd(document.getElementById("reply_insert_form"));
}

function placeCaretAtEnd(el) {
	el.focus();
	if (typeof window.getSelection != "undefined" && typeof document.createRange != "undefined") {
		const range = document.createRange();
		range.selectNodeContents(el);
		range.collapse(false);
		const sel = window.getSelection();
		sel.removeAllRanges();
		sel.addRange(range);
	}
}
function getTimeAgo(minute) {
	if (minute <= 59) return getMsg('msg_reply_time_minutes_ago').replace('{0}', minute);
	if (minute <= 1440) return getMsg('msg_reply_time_hours_ago').replace('{0}', Math.trunc(minute / 60));
	return getMsg('msg_reply_time_days_ago').replace('{0}', Math.trunc(minute / 60 / 24));
}

function getBoardReply(board_id) {
	const session_user_id = $("#session_user_id").text();

	$.ajax({
		type: "GET",
		url: "/board/reply/boardReply",
		data: { board_id },
		success: function(res) {
			const wrapper = $(".board_reply_wrapper");
			wrapper.empty();

			const mainReplies = res.filter(r => r.reply_status === 'reply');
			$("#comment-total-count").text(mainReplies.length);

			const grouped = {};
			res.forEach(r => {
				if (!grouped[r.reply_id]) grouped[r.reply_id] = [];
				grouped[r.reply_id].push(r);
			});

			Object.values(grouped).forEach(group => {
				const main = group.find(r => r.reply_status === 'reply');
				if (!main) return;

				const isMine = main.reply_writer === session_user_id;
				const replyId = main.reply_id;
				const rereplyCount = group.filter(r => r.reply_status.startsWith("rereply")).length;
				const timeAgo = getTimeAgo(main.reply_date_differ);
				const hasEmoticon = main.reply_content.includes("<emoticon") || main.reply_content.includes("<img>");
				const isOnlyEmoticon = main.reply_content.trim().match(/^<emoticon name="[^"]+"><\/emoticon>$/) || main.reply_content.trim().match(/^<img[^>]+>$/);

				let html = `
					<div class="comment-item" data-id="${replyId}" data-status="reply">
						<div class="comment-meta">
							<span class="comment-user">${main.user_nickname}</span>
							<span class="comment-date">${timeAgo}</span>
						</div>
						<div class="comment-content">${main.reply_content}</div>
						<div class="original-content" style="display:none;">${main.reply_content}</div>
						<div class="comment-actions">
							${isMine ? `
								${!isOnlyEmoticon ? `<button class="replyUpdateBtn">${getMsg('msg_reply_update_button')}</button>` : ""}
								<button class="replyDeleteBtn">${getMsg('msg_reply_delete_button')}</button>
							` : ""}
							<button class="replyReplyBtn">${getMsg('msg_reReply_button')} (${rereplyCount})</button>
							${!hasEmoticon ? `<button class="translate_reply-btn">${getMsg('msg_translate_button')}</button>` : ""}
						</div>
						${isMine && !isOnlyEmoticon ? `
						<div class="comment-update-box" style="display:none;">
							<textarea class="update-form" rows="3">${main.reply_content}</textarea>
							<button class="replyUpdateConfirmBtn">${getMsg('msg_reply_update_button')}</button>
						</div>` : ""}
					</div>
				`;

				wrapper.prepend(html);
				renderEmoticonsIn(wrapper.find(".comment-content").first());
			});
		}
	});
}


$(document).ready(function() {
	$("#reply_insert_btn").click(function() {
		const content = $("#reply_insert_form").html().trim();
		const session_user_id = $("#session_user_id").text();

		if (!content) return;
		if (content.length > 200) {
			alert(getMsg("msg_reply_validation_message"));
			return;
		}

		$.post("/board/reply/boardReplyInsert", {
			reply_content: content,
			reply_writer: session_user_id,
			board_id: board_id,
		}).done(() => {
			getBoardReply(board_id);
			$("#reply_insert_form").html("").attr("contenteditable", true).css("cursor", "").css("opacity", "");
			$("#openEmoticonModalBtn").prop("disabled", false).css("cursor", "").css("opacity", "");
		});
	});

	$("#openEmoticonModalBtn").click(function() {
		$.get("/emoticon/approvedList", function(list) {
			emoticonMap = {};
			const html = list.map(e => {
				emoticonMap[e.emoticon_name] = e.emoticon_file_name;
				return `<img src="/uploads/emoticon/${e.emoticon_file_name}"
					alt="${e.emoticon_name}"
					data-name="${e.emoticon_name}"
					style="width:100px; cursor:pointer; margin:4px;">`;
			}).join("");
			$("#emoticonList").html(html);
			$("#emoticonModal").show();
		});
	});

	$(document).on("click", "#emoticonList img", function() {
		const tag = `<emoticon name="${$(this).data("name")}"></emoticon>`;
		const editor = $("#reply_insert_form");

		if (editor.find("img").length > 0) return;

		editor.html(tag);
		renderInputEmoticons();

		editor.attr("contenteditable", false).css("cursor", "not-allowed").css("opacity", 0.8);
		$("#openEmoticonModalBtn").prop("disabled", true).css("cursor", "not-allowed").css("opacity", 0.5);

		$("#emoticonModal").hide();
	});
	$(document).on("click", ".replyDeleteBtn", function() {
		const container = $(this).closest(".comment-item");
		const reply_id = container.data("id");
		const reply_status = container.data("status");

		if (!confirm(getMsg('msg_reply_delete_confirm'))) return;

		$.post("/board/reply/boardReplyDelete", {
			reply_id,
			reply_status,
		}).done(() => getBoardReply(board_id));
	});

	$(document).on("click", ".replyUpdateBtn", function() {
		$(".comment-update-box").hide();
		const box = $(this).closest(".comment-item").find(".comment-update-box");
		box.show();
	});

	$(document).on("click", ".replyUpdateConfirmBtn", function() {
		const container = $(this).closest(".comment-item");
		const reply_id = container.data("id");
		const reply_status = container.data("status");
		const content = container.find(".update-form").val();

		$.post("/board/reply/boardReplyUpdate", {
			reply_content: content,
			reply_id,
			reply_status,
			board_id,
		}).done(() => getBoardReply(board_id));
	});

	$(document).on("click", ".replyReplyBtn", function() {
		const session_user_id = $("#session_user_id").text();
		const container = $(this).closest(".comment-item");
		const reply_id = container.data("id");

		// ⚠️ replies-container가 없을 경우 동적으로 생성
		let repliesBox = container.find(".replies-container");
		if (repliesBox.length === 0) {
			container.append(`<div class="replies-container" style="display:none;"></div>`);
			repliesBox = container.find(".replies-container");
		}

		if (repliesBox.children().length === 0) {
			$.get("/board/reply/boardReply", { board_id }, function(res) {
				const replies = res.filter(r => r.reply_id === reply_id && r.reply_status.startsWith("rereply"));
				replies.sort((a, b) => parseInt(a.reply_status.replace("rereply", "")) - parseInt(b.reply_status.replace("rereply", "")));

				replies.forEach(rr => {
					const isMine = rr.reply_writer === session_user_id;
					const hasEmoticon = rr.reply_content.includes("<emoticon") || rr.reply_content.includes("<img>");
					const isOnlyEmoticon = rr.reply_content.trim().match(/^<emoticon name="[^"]+"><\/emoticon>$/) || rr.reply_content.trim().match(/^<img[^>]+>$/);

					let html = `
						<div class="comment-item reply-indent" data-id="${rr.reply_id}" data-status="${rr.reply_status}">
							<div class="comment-meta">
								<span class="comment-user">${rr.user_nickname}</span>
								<span class="comment-date">${getTimeAgo(rr.reply_date_differ)}</span>
							</div>
							<div class="comment-content">${rr.reply_content}</div>
							<div class="original-content" style="display:none;">${rr.reply_content}</div>
							<div class="comment-actions">
								${isMine ? `
									${!isOnlyEmoticon ? `<button class="replyUpdateBtn">${getMsg('msg_reply_update_button')}</button>` : ""}
									<button class="replyDeleteBtn">${getMsg('msg_reply_delete_button')}</button>
								` : ""}
								${!hasEmoticon ? `<button class="translate_reply-btn">${getMsg('msg_translate_button')}</button>` : ""}
							</div>
							<div class="comment-update-box" style="display:none;">
								<textarea class="update-form" rows="3">${rr.reply_content}</textarea>
								<button class="replyUpdateConfirmBtn">${getMsg('msg_reply_update_button')}</button>
							</div>
						</div>
					`;

					repliesBox.append(html);
					renderEmoticonsIn(repliesBox.find(".comment-content").last());
				});

				repliesBox.append(`
					<div class="comment-reply-box">
						<textarea class="reply-form" rows="3" placeholder="${getMsg("msg_reply_placeholder")}"></textarea>
						<button class="replyReplyConfirmBtn">${getMsg('msg_reply_insert_button')}</button>
					</div>
				`);

				repliesBox.slideDown();
			});
		} else {
			repliesBox.slideToggle();
		}
	});



	$(document).on("click", ".replyReplyConfirmBtn", function() {
		const container = $(this).closest(".comment-item");
		const reply_id = container.data("id");
		const content = container.find(".reply-form").val().trim();
		const session_user_id = $("#session_user_id").text();

		if (!content) return;
		if (content.length > 200) {
			alert(getMsg("msg_reply_validation_message"));
			return;
		}

		$.post("/board/reply/boardRereplyInsert", {
			reply_content: content,
			reply_writer: session_user_id,
			reply_id,
			board_id,
		}).done(() => getBoardReply(board_id));
	});

	$(document).on("click", ".translate_reply-btn", function() {
		const replyBlock = $(this).closest(".comment-item");
		const textElem = replyBlock.find(".comment-content").eq(0);
		const originalText = replyBlock.find(".original-content").eq(0).text().trim();

		if (!originalText) return;

		if (textElem.hasClass("translated-ja")) {
			textElem.html(originalText);
			textElem.removeClass("translated-ja");
			renderEmoticonsIn(textElem);
		} else {
			fetch("/api/translate", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ input: originalText })
			}).then(res => res.text()).then(translated => {
				textElem.text(translated + " (" + getMsg("msg_translate_button") + ")");
				textElem.addClass("translated-ja");
			});
		}
	});
});
