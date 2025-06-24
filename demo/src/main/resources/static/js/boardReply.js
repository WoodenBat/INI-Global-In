/**
 * 
 */

let localeClass = "";

window.onload = function() {
	localeClass = $("#localeClass").text().trim();
	console.log("페이지 로딩 끝");
	console.log(localeClass)
	getBoardReply(board_id);
};

function getBoardReply(board_id) {
	const sessionNickname = $("#session_nickname").text();
	$.ajax({
		type: "GET",
		url: "/board/reply/boardReply",
		data: {
			board_id: board_id
		},
		success: function(response) {
			console.log("성공:", response);
			console.log(response);
			$(".board_reply_wrapper").empty();

			response.forEach(function(br) {
				let html = '';
				console.log(br);
				if (br.reply_status === 'reply') {
					html += `
                        <div class="board_reply_container">
                    		<span id="reply_id" hidden="true" style="display: none">`+ br.reply_id + `</span>
                    		<span id="reply_status" hidden="true" style="display: none">`+ br.reply_status + `</span>
                            <div class="board_reply_info">
                                <img class="board_reply_profileImg" alt="프로필 사진" src="/images/cat.png">
                                <span class="board_reply_userNickName">${br.user_nickname}</span>
                                <span class="board_reply_write_date">${getTimeAgo(br.reply_date_differ)}</span>
                                <div class="board_reply_report_btn">
                                    <img alt="신고버튼" src="/img/icon/reply_report_icon.png" />
                                </div>
                                <button class="translate-btn">번역 테스트</button>
                            </div>
                            <div class="board_reply_content_container">
                                <div class="board_reply_content">${br.reply_content}</div>
                                <div class="board_reply_content_hidden" style="display: none;"></div>
                            </div>
                                `;
					if (br.reply_writer === sessionNickname) {
						html += `
                        	<div class="board_reply_button_container">
       		                  	<button id="replyUpdate" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reply_update_button").text() + `</button>
       		                 	<button id="replyDelete" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reply_delete_button").text() + `</button>
                         	  	<button id="replyRereply" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reReply_insert_button").text() + `</button>
           		                 `;
					} else {
						html += `
       		               		<div class="board_reply_button_container_other">
                       	  			<button id="replyRereply" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reReply_insert_button").text() + `</button>
         		                 `;
					}
					html += `
	                            </div>
	                        </div>
                        <div class="board_reply_reReply_insert_container" style="display:none">
                        	<textarea id="reReplyInsertForm" class="board_reply_reReply_insert_form"></textarea>
                        	<button id="reReplyInsertBtn" class="board_reply_reReply_insert_btn">작성</button>
                    	</div>
                    	<div class="board_reply_update_container" style="display:none">
                        	<textarea id="replyUpdateForm" class="board_reply_reReply_insert_form"></textarea>
                        	<button id="replyUpdateBtn" class="board_reply_reReply_insert_btn">수정</button>
                        </div>
                    `;
				} else {
					html += `
                        <div class="board_reply_reReply_container">
                            <span id="reply_id" hidden="true" style="display: none">`+ br.reply_id + `</span>
                    		<span id="reply_status" hidden="true" style="display: none">`+ br.reply_status + `</span>
                            <div class="board_reply_reReply_box">
                            	<div class="board_reply_reReply_icon_container">
                            		<img alt="대댓글 아이콘" src="/img/icon/reReply_icon.png" />
                            	</div>
                            	<div class="board_reply_container_inner">
	                                <div class="board_reply_reReply_info">
	                                    <img class="board_reply_profileImg" alt="프로필 사진" src="/images/cat.png">
	                                    <span class="board_reply_userNickName">${br.user_nickname}</span>
	                                    <span class="board_reply_write_date">${getTimeAgo(br.reply_date_differ)}</span>
	                                    <div class="board_reply_report_btn">
	                                        <img alt="신고버튼" src="/img/icon/reply_report_icon.png" />
	                                    </div>
	                                    <button class="translate-btn">번역 테스트</button>
	                                </div>
	                                <div class="board_reply_content_container">
	                                    <div class="board_reply_content">${br.reply_content}</div>
	                                    <div class="board_reply_content_hidden" style="display: none;"></div>
	                                </div>
	                                `;
					if (br.reply_writer === sessionNickname) {
						html += `
                                	<div class="board_reply_button_container">
										<button id="replyUpdate" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reply_update_button").text() + `</button>
	           		                 	<button id="replyDelete" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reply_delete_button").text() + `</button>
	                             	  	<button id="replyRereply" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reReply_insert_button").text() + `</button>
	               		                 `;
					} else {
						html += `
	           		               		<div class="board_reply_button_container_other">
	                           	  			<button id="replyRereply" type="button" class="board_reply_button ${localeClass}">` + $("#msg_reReply_insert_button").text() + `</button>
	             		                 `;
					}
					html += `
		                                </div>
	                                </div>
	                            </div>
	                        </div>
                        <div class="board_reply_reReply_insert_container" style="display:none">
                        	<textarea id="reReplyInsertForm" class="board_reply_reReply_insert_form"></textarea>
                        	<button id="reReplyInsertBtn" class="board_reply_reReply_insert_btn">작성</button>
                        </div>
                        <div class="board_reply_update_container" style="display:none">
                        	<textarea id="replyUpdateForm" class="board_reply_reReply_insert_form"></textarea>
                        	<button id="replyUpdateBtn" class="board_reply_reReply_insert_btn">수정</button>
                        </div>
                    `;
				}
				$(".board_reply_wrapper").append(html);
			});
		},
		error: function(xhr, status, error) {
			console.error("에러:", error);
		}
	});

	function getTimeAgo(minute) {

		if (minute <= 59) {
			return minute + "분 전";
		} else if (minute <= 1440) {

			return Math.trunc(minute / 60) + "시간 전";

		} else {

			return Math.trunc(minute / 60 / 24) + "일 전";
		}

	}
}


$(document).ready(function() {
	$("#reply_insert_btn").click(function() {
		const sessionNickname = $("#session_nickname").text();
		console.log(sessionNickname);
		$.ajax({
			type: "POST",
			url: "/board/reply/boardReplyInsert",
			data: {
				reply_content: $("#reply_insert_form").val(),
				reply_writer: sessionNickname,
				board_id: board_id,
			},
			success: function() {
				getBoardReply(board_id);
			},
			error: function(xhr, status, error) {
				console.error("에러:", error);
			}
		})

		$("#reply_insert_form").val("");
	});

	$(document).on("click", "#replyDelete", function() {
		const container = $(this).closest(".board_reply_container, .board_reply_reReply_container");
		const reply_id = container.find("span#reply_id").text().trim();
		const reply_status = container.find("span#reply_status").text().trim();

		if (confirm("정말 삭제하시겠습니까?")) {
			$.ajax({
				type: "POST",
				url: "/board/reply/boardReplyDelete",
				data: {
					reply_id: reply_id,
					reply_status: reply_status,
				},
				success: function() {
					getBoardReply(board_id);
				},
				error: function(xhr, status, error) {
					console.error("삭제 실패:", error);
				}
			});

		} else {
			return;
		}

	});

	$(document).on("click", "#replyRereply", function() {

		$(".board_reply_reReply_insert_container").hide();
		$(".board_reply_update_container").hide();

		const container = $(this).closest(".board_reply_container, .board_reply_reReply_container");
		const inputBox = container.next(".board_reply_reReply_insert_container");

		if (container.hasClass("board_reply_container")) {
			inputBox.toggle();

		} else {
			inputBox.css("margin-left", "67px");
			inputBox.toggle();

		}
	});

	$(document).on("click", "#replyUpdate", function() {

		$(".board_reply_reReply_insert_container").hide();
		$(".board_reply_update_container").hide();

		const container = $(this).closest(".board_reply_container, .board_reply_reReply_container");
		const inputBox = container.nextAll(".board_reply_update_container").eq(0);
		let original_reply = container.find(".board_reply_content").text();
		
		if(original_reply.includes("(번역)")) {
			original_reply = container.find(".board_reply_content_hidden").text();
		}
		
		inputBox.find(".board_reply_reReply_insert_form").text(original_reply);
		
		if (container.hasClass("board_reply_container")) {
			inputBox.toggle();

		} else {
			inputBox.css("margin-left", "67px");
			inputBox.toggle();

		}

	});

	$(document).on("click", "#reReplyInsertBtn", function() {
		const sessionNickname = $("#session_nickname").text();
		const container = $(this).closest(".board_reply_reReply_insert_container").prev();
		const reply_id = container.find("span#reply_id").text().trim();
		const reply_content = $(this).siblings("#reReplyInsertForm").val();
		console.log(reply_id);

		$.ajax({
			type: "POST",
			url: "/board/reply/boardRereplyInsert",
			data: {
				reply_content: reply_content,
				reply_writer: sessionNickname,
				reply_id: reply_id,
				board_id: board_id,
			},
			success: function() {
				getBoardReply(board_id);
			},
			error: function(xhr, status, error) {
				console.error("에러:", error);
			}
		})

	});

	$(document).on("click", "#replyUpdateBtn", function() {
		const container = $(this).closest(".board_reply_update_container").prev().prev();
		const reply_id = container.find("span#reply_id").text().trim();
		const reply_status = container.find("span#reply_status").text().trim();
		const reply_content = $(this).siblings("#replyUpdateForm").val();
		container.find(".board_reply_content_hidden").text("");
		Object.keys(translationCache).forEach(key => delete translationCache[key]);
		console.log(reply_id);
		console.log(reply_status);

		$.ajax({
			type: "POST",
			url: "/board/reply/boardReplyUpdate",
			data: {
				reply_content: reply_content,
				reply_id: reply_id,
				reply_status: reply_status,
				board_id: board_id,
			},
			success: function() {
				getBoardReply(board_id);
			},
			error: function(xhr, status, error) {
				console.error("에러:", error);
			}
		})
	});

})