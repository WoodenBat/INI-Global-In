document.addEventListener("DOMContentLoaded", function() {
	// 승인
	document.querySelectorAll(".approve_btn").forEach(btn => {
		btn.addEventListener("click", function() {
			const name = this.dataset.name;
			approve(name);
		});
	});

	// 거절
	document.querySelectorAll(".reject_btn").forEach(btn => {
		btn.addEventListener("click", function() {
			const name = this.dataset.name;
			reject(name);
		});
	});

	// 삭제
	document.querySelectorAll(".delete_btn").forEach(btn => {
		btn.addEventListener("click", function() {
			const name = this.dataset.name;
			deleteEmoticon(name);
		});
	});

	// 이미지 클릭 → 모달 열기
	document.querySelectorAll(".emoticon_image").forEach(img => {
		img.addEventListener("click", function() {
			const name = this.dataset.name;
			openModal(name);
		});
	});

	// 모달 닫기
	document.getElementById("modalClose").addEventListener("click", function() {
		closeModal();
	});
});

function approve(emoticonName) {
	fetch(`/admin/emoticons/approve`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ emoticon_name: emoticonName })
	})
		.then(response => response.text())
		.then(() => {
			alert("승인 완료");
			location.reload();
		});
}

function reject(emoticonName) {
	fetch(`/admin/emoticons/reject`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ emoticon_name: emoticonName })
	})
		.then(response => response.text())
		.then(() => {
			alert("거절 완료");
			location.reload();
		});
}

function deleteEmoticon(emoticonName) {
	fetch(`/admin/emoticons/delete`, {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({ emoticon_name: emoticonName })
	})
		.then(response => response.text())
		.then(() => {
			alert("삭제 완료");
			location.reload();
		});
}

function openModal(emoticonName) {
	fetch(`/admin/emoticons/info?name=${encodeURIComponent(emoticonName)}`)
		.then(res => res.json())
		.then(data => {
			const modal = document.getElementById("infoModal");
			const content = document.getElementById("modalContent");

			content.innerHTML = `
				<strong>이름:</strong> ${data.emoticon_name}<br>
				<strong>파일명:</strong> ${data.emoticon_file_name}<br>
				<strong>작성자:</strong> ${data.emoticon_creator}<br>
				<strong>작성일:</strong> ${data.emoticon_create_date}<br>
				<strong>상태:</strong> ${data.emoticon_status}
			`;

			modal.style.display = "block";
		});
}

function closeModal() {
	document.getElementById("infoModal").style.display = "none";
}
