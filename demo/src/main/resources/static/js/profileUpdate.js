/** @type {HTMLInputElement} */

let nicknameChecked = true;
let passwordChecked = true;
const original_nickname = $("#user_nickname_input").val()

$("#user_nickname_input").change(function(){
	if($(this).val() === original_nickname) {
		nicknameChecked = true;
		$("#nickname_check_btn").css("display", "none")
	} else {
		nicknameChecked = false;
		$("#nickname_check_btn").css("display", "inline")
	}
})

$("#submit_btn").click(function(e) {
	e.preventDefault();

	if (nicknameChecked === false) {
		alert("닉네임 중복확인을 해주세요")
		return;
		
	} else if (passwordChecked === false){
		alert("비밀번호를 확인해주세요")
		return;
		
	} else {
		$("#profileUpdateForm").submit();
	}
})

$("#user_password_input, #user_password_check_input").change(function() {
	const password = $("#user_password_input").val();
	const password_check = $("#user_password_check_input").val();

	if (password === password_check) {
		passwordChecked = true;
		$("#password_check_img").css("display", "inline");
	} else {
		passwordChecked = false;
		$("#password_check_img").css("display", "none");
	}
})

document.getElementById("profileUploadInput").addEventListener("change", function(e) {
	const file = e.target.files[0];
	if (!file)
		return;

	const reader = new FileReader();
	reader.onload = function(event) {
		const imgTag = document
			.querySelector(".profile_image_preview");
		imgTag.src = event.target.result;
	};
	reader.readAsDataURL(file);
});


function checkDuplicate(type) {
	const inputMap = {
		nickname: 'user_nickname'
	};
	const inputName = inputMap[type];
	const value = document.getElementById("user_nickname_input").value.trim();

	if (!value) {
		alert(type + "를 입력해주세요!");
		return;
	}

	fetch(`/member/check${type.charAt(0).toUpperCase() + type.slice(1)}?${inputName}=` + encodeURIComponent(value))
		.then(res => res.json())
		.then(data => {
			if (data.exists) {
				alert(type + "가 이미 존재합니다.");
				nicknameChecked = false;
			} else {
				alert("사용 가능한 " + type + "입니다!");
				nicknameChecked = true;
			}
		})
		.catch(err => {
			console.error(err);
			alert("중복 확인 중 오류가 발생했습니다.");
		});
}

function checkNicknameLength(input) {
	const maxLength = 18;
	if (input.value.length > maxLength) {
		alert("닉네임은 18자 이내로 입력해주세요.");
		input.value = input.value.substring(0, maxLength);
	}
}