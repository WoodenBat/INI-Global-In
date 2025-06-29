/**
 * 
 */

let localeClass = "";

window.onload = function() {
	localeClass = $("#localeClass").text().trim();
	getBoardReply(board_id);

	document.getElementById("reply_insert_form").setAttribute("placeholder", getMsg("msg_reply_placeholder"));
	
	const session_user_id = $("#session_user_id").text().trim();
	if (!session_user_id) {
		$("#reply_insert_form").prop("disabled", true);
		$("#reply_insert_btn").prop("disabled", true).css("cursor", "not-allowed").css("opacity", 0.5);
		$("#reply_login_msg").show();
	}
};

function getMsg(id) {
	return document.getElementById(id)?.textContent?.trim() || '';
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

			// 댓글 수 카운트
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
				const timeAgo = getTimeAgo(main.reply_date_differ);
				const replyId = main.reply_id;
				const rereplyCount = group.filter(r => r.reply_status.startsWith("rereply")).length;

				let html = `
				<div class="comment-item" data-id="${replyId}" data-status="reply">
				    <div class="comment-meta">
				      <span class="comment-user">${main.user_nickname}</span>
				      <span class="comment-date">${timeAgo}</span>
				    </div>
				    <div class="comment-content">${escapeHTML(main.reply_content)}</div>
				    <div class="original-content" style="display:none;">${escapeHTML(main.reply_content)}</div>
				    <div class="comment-actions">
				      ${isMine
						? `<button class="replyUpdateBtn">${getMsg('msg_reply_update_button')}</button>
				           <button class="replyDeleteBtn">${getMsg('msg_reply_delete_button')}</button>`
						: ""}
				      <button class="replyReplyBtn">${getMsg('msg_reReply_button')} (${rereplyCount})</button>
				      <button class="translate_reply-btn">${getMsg('msg_translate_button')}</button>
				    </div>
				    <div class="comment-update-box" style="display: none;">
				      <textarea class="update-form" rows="3">${main.reply_content}</textarea>
				      <button class="replyUpdateConfirmBtn">${getMsg('msg_reply_update_button')}</button>
				    </div>
				    <div class="replies-container" style="display: none;"></div>
				  </div>
				`;

				wrapper.prepend(html);
			});
		}
	});
}

function getTimeAgo(minute) {
	if (minute <= 59) return getMsg('msg_time_minutes').replace('{0}', minute);
	if (minute <= 1440) return getMsg('msg_time_hours').replace('{0}', Math.trunc(minute / 60));
	return getMsg('msg_time_days').replace('{0}', Math.trunc(minute / 60 / 24));
}

function escapeHTML(str) {
	return str.replace(/[&<>'"]/g, tag => ({
		'&': '&amp;',
		'<': '&lt;',
		'>': '&gt;',
		"'": '&#39;',
		'"': '&quot;'
	}[tag]));
}

$(document).ready(function() {
	$("#reply_insert_btn").click(function() {
		const content = $("#reply_insert_form").val().trim();
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
			$("#reply_insert_form").val("");
		});
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
		let repliesBox = container.find(".replies-container");

		if (repliesBox.children().length === 0) {
			$.get("/board/reply/boardReply", { board_id }, function(res) {
				const replies = res.filter(r => r.reply_id === reply_id && r.reply_status.startsWith("rereply"));
				replies.sort((a, b) => parseInt(a.reply_status.replace("rereply", "")) - parseInt(b.reply_status.replace("rereply", "")));

				replies.forEach(rr => {
					const isMine = rr.reply_writer === session_user_id;
					const replyStatus = rr.reply_status;
					const replyId = rr.reply_id;

					repliesBox.append(`
						  <div class="comment-item reply-indent" data-id="${replyId}" data-status="${replyStatus}">
						    <div class="comment-meta">
						      <span class="comment-user">${rr.user_nickname}</span>
						      <span class="comment-date">${getTimeAgo(rr.reply_date_differ)}</span>
						    </div>
						    <div class="comment-content">${escapeHTML(rr.reply_content)}</div>
						    <div class="original-content" style="display:none;">${escapeHTML(rr.reply_content)}</div>
						    <div class="comment-actions">
						      ${isMine
							? `<button class="replyUpdateBtn">${getMsg('msg_reply_update_button')}</button>
						           <button class="replyDeleteBtn">${getMsg('msg_reply_delete_button')}</button>`
							: ""}
						      <button class="translate_reply-btn">${getMsg('msg_translate_button')}</button>
						    </div>
						    <div class="comment-update-box" style="display: none;">
						      <textarea class="update-form" rows="3">${rr.reply_content}</textarea>
						      <button class="replyUpdateConfirmBtn">${getMsg('msg_reply_update_button')}</button>
						    </div>
						  </div>
						`);
				});
				const isLoggedIn = !!session_user_id;

				repliesBox.append(`
						<div class="comment-reply-box">
						    <textarea class="reply-form" rows="3" ${!isLoggedIn ? 'disabled' : ''} placeholder="${!isLoggedIn ? getMsg("msg_reply_not_logged_in") : ''}"></textarea>
							<button class="replyReplyConfirmBtn" ${!isLoggedIn ? 'disabled style="cursor: not-allowed; opacity: 0.5;"' : ''}>
							  ${getMsg('msg_reply_insert_button')}
							</button>
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
			textElem.text(originalText);
			textElem.removeClass("translated-ja");
		} else {
			fetch("/api/translate", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ input: originalText })
			})
				.then(res => res.text())
				.then(translated => {
					textElem.text(translated + " (" + getMsg("msg_translate_button") + ")");
					textElem.addClass("translated-ja");
				});
		}
	});
});


function adjustFont(elem) {
	const txt = elem.text();
	const isJapanese = /[\u3040-\u30FF\u4E00-\u9FFF]/.test(txt);
	if (isJapanese) {
		elem.addClass("translated-ja");
	} else {
		elem.css("font-size", "20px");
	}
}