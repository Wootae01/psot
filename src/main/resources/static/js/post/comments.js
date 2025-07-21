
//대댓글 추가
document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.comment-item.toggle-reply-btn').forEach(item => {
        item.addEventListener('click', (e) => {
            // 답글 폼 내부 클릭 시 토글 로직이 실행되는 걸 방지
            if (e.target.closest('.reply-box')) return;

            const existing = item.querySelector('.reply-box');
            if (existing) {
                existing.remove();
                return;
            }

            // 기존 폼 전부 제거(안정성)
            item.querySelectorAll('.reply-box').forEach(el => el.remove());

            // 템플릿 복제 후 parentCommentId 세팅
            const tpl = document.getElementById('reply-form-template').firstElementChild;
            const clone = tpl.cloneNode(true);
            clone.querySelector('input[name="parentCommentId"]').value =
                item.getAttribute('data-comment-id');
            item.appendChild(clone);
        });
    });
});