
const translationCache_board = {};
const translationCache_reply = new Map();
//버튼 여러개 인덱스 설정
/*
document.querySelectorAll('.translate-btn').forEach((btn, index) => {
	btn.addEventListener('click', () => {//버튼 클릭하면 실행
		const replyBlock = btn.closest('.board_reply_content_container'); //replyblock찾아서
		const textElem = replyBlock.querySelector('.board_reply_content'); //reply-text태그 찾아서
		const replyId = index;  // 캐시메모리에서 사용할 고유 키용
		const originalText = textElem.textContent; //텍스트 가져오기

		if (translationCache[replyId]) { //캐시메모리에 데이터 유무 검사
			showPopup(translationCache[replyId]); //팝업뛰우기
		} else {
			fetch("/api/translate", { //없으면 컨트롤러에서 gptapi요청
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ input: originalText })
			})
				.then(res => res.text()) //응답받아서 파싱
				.then(translated => { //string으로 받음
					translationCache[replyId] = translated;
					showPopup(translated);
				})
				.catch(() => showPopup(originalText, "번역 실패"));
		}
	});
});
*/

$(document).on("click", ".translate-btn", function() {
	const btn = $(this);
	console.log(btn);
	const replyBlock = btn.closest('.board_reply_info, .board_reply_reReply_info').next('.board_reply_content_container');
	console.log(replyBlock);
	const textElem = replyBlock.find('.board_reply_content');
	const unTransText = textElem.next('.board_reply_content_hidden');
	console.log(textElem);
	const replyId = $(".translate-btn").index(this);  // 인덱스로 고유 식별
	console.log(replyId);

	const originalText = textElem.text();
	if (unTransText.text() === "") {

		if (translationCache_reply[replyId]) {
			unTransText.text(textElem.text());
			textElem.text(translationCache_reply[replyId] + ' (번역)');
			textElem.css('font-size', '23px');
		} else {
			fetch("/api/translate", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ input: originalText })
			})
				.then(res => res.text())
				.then(translated => {
					translationCache_reply[replyId] = translated;
					//showPopup(translated);
					unTransText.text(textElem.text());
					textElem.text(translated + ' (번역)');
					textElem.css('font-size', '23px');
				})
				.catch(() => showPopup(originalText, "번역 실패"));
		}
	} else {
		textElem.text(unTransText.text());
		unTransText.text("");
		textElem.css('font-size', '30px');
	}
});

/*
$(document).on("click", ".translate_board-btn", function() {
	const btn = $(this);
	console.log(btn);
	const replyBlock = btn.closest('.reply-block');
	console.log(replyBlock);
	const textElem = replyBlock.find('.reply-text');
	const unTransText = textElem.next('.board_content_hidden');
	console.log(textElem);
	const board_id = $(".translate_board-btn").index(this);  // 인덱스로 고유 식별
	console.log(board_id);

	const originalText = textElem.text();
	if (unTransText.text() === "") {

		if (translationCache_board[board_id]) {
			unTransText.text(textElem.text());
			textElem.text(translationCache_board[board_id] + ' (번역)');
			textElem.css('font-size', '15px');
		} else {
			fetch("/api/translate", {
				method: "POST",
				headers: { "Content-Type": "application/json" },
				body: JSON.stringify({ input: originalText })
			})
				.then(res => res.text())
				.then(translated => {
					translationCache_board[board_id] = translated;
					unTransText.text(textElem.text());
					textElem.text(translated + ' (번역)');
					textElem.css('font-size', '15px');
				})
				.catch(() => showPopup(originalText, "번역 실패"));
		}
	} else {
		textElem.text(unTransText.text());
		unTransText.text("");
		textElem.css('font-size', '15px');
	}
}); 
*/

function showPopup(translated) { //팝업
	const popup = document.createElement("div");
	popup.className = "floating-window";
	popup.innerHTML = `
    <div class="header">번역결과 <button onclick="this.parentNode.parentNode.remove()">✖</button></div>
    <div class="body">
      <strong>번역:</strong><p>${translated}</p>
    </div>`;
	document.body.appendChild(popup);

	// 드래그 이동 기능
	let offsetX, offsetY;
	const header = popup.querySelector('.header');
	header.addEventListener('mousedown', e => {
		offsetX = e.clientX - popup.offsetLeft;
		offsetY = e.clientY - popup.offsetTop;
		document.onmousemove = e => {
			popup.style.left = `${e.clientX - offsetX}px`;
			popup.style.top = `${e.clientY - offsetY}px`;
		};
		document.onmouseup = () => (document.onmousemove = null);
	});
}
