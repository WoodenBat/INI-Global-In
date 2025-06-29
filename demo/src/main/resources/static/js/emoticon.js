let cropper;

document.addEventListener("DOMContentLoaded", function() {
	const fileInput = document.getElementById("fileInput");
	const cropperModal = document.getElementById("cropperModal");
	const cropperImage = document.getElementById("cropperImage");
	const cropCanvas = document.getElementById("cropCanvas");
	const cropConfirmBtn = document.getElementById("cropConfirmBtn");
	const cropCancelBtn = document.getElementById("cropCancelBtn");

	if (!fileInput || !cropperModal || !cropperImage || !cropCanvas || !cropConfirmBtn || !cropCancelBtn) {
		console.error("필수 요소 중 하나가 누락됨");
		return;
	}


	fileInput.addEventListener("change", function(e) {
		const file = e.target.files[0];
		if (file && file.size > 2 * 1024 * 1024) {
			alert("2MB 이하 이미지 파일만 업로드 가능합니다.");
			e.target.value = "";
			return;
		}

		if (!file) return;

		const reader = new FileReader();
		reader.onload = function() {
			cropperImage.src = reader.result;
			cropperModal.style.display = "flex";

			if (cropper) cropper.destroy();
			cropper = new Cropper(cropperImage, {
				aspectRatio: 1,
				viewMode: 1,
				autoCropArea: 1
			});
		};
		reader.readAsDataURL(file);
	});

	cropConfirmBtn.addEventListener("click", function() {
		const canvas = cropper.getCroppedCanvas();
		canvas.toBlob(function(blob) {
			const file = new File([blob], "cropped_image.png", { type: "image/png" });

			const dataTransfer = new DataTransfer();
			dataTransfer.items.add(file);
			fileInput.files = dataTransfer.files;

			cropper.destroy();
			cropperModal.style.display = "none";
		});
	});

	cropCancelBtn.addEventListener("click", function() {
		cropper.destroy();
		cropperModal.style.display = "none";
		fileInput.value = "";
	});
});
const previewImage = document.getElementById("previewImage");

cropConfirmBtn.addEventListener("click", function() {
	const canvas = cropper.getCroppedCanvas();
	canvas.toBlob(function(blob) {
		const file = new File([blob], "cropped_image.png", { type: "image/png" });

		const dataTransfer = new DataTransfer();
		dataTransfer.items.add(file);
		fileInput.files = dataTransfer.files;

		// 미리보기 이미지 설정
		previewImage.src = canvas.toDataURL();
		previewImage.style.display = "block";

		cropper.destroy();
		cropperModal.style.display = "none";
	});
});
