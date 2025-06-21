document.addEventListener('DOMContentLoaded', () => {
  const translationCache = {};

  document.querySelectorAll('.translate-btn').forEach((btn, index) => {
    btn.addEventListener('click', () => {
      const replyBlock = btn.closest('.reply-block');  // 고정으로 가져옴
      if (!replyBlock) {
        console.warn("reply-block을 찾을 수 없습니다.");
        return;
      }

      const textElem = replyBlock.querySelector('.reply-text');
      if (!textElem) {
        console.warn("reply-text 요소가 없습니다.");
        return;
      }

      const replyId = index;
      const originalText = textElem.textContent;

      if (translationCache[replyId]) {
        showPopup(translationCache[replyId]);
      } else {
        fetch("/api/translate", {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ input: originalText }) 
        })
        .then(res => res.text())
        .then(translated => {
          translationCache[replyId] = translated;
          showPopup(translated);
        })
        .catch(() => showPopup(originalText + "\n(번역 실패)"));
      }
    });
  });

  function showPopup(translated) {
    const popup = document.createElement("div");
    popup.className = "floating-window";
    popup.innerHTML = `
      <div class="header">번역결과 <button onclick="this.parentNode.parentNode.remove()">✖</button></div>
      <div class="body"><strong>번역:</strong><p>${translated}</p></div>
    `;
    document.body.appendChild(popup);

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
});
