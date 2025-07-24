// comments.js
document.addEventListener('DOMContentLoaded', () => {
    const commentList = document.getElementById('commentList');
    const rootForm    = document.getElementById('commentForm');
    const postId      = rootForm.dataset.postId;

    // 루트 댓글에만 클릭 시 대댓글 입력 화면 토글
    commentList.addEventListener('click', e => {

        const item = e.target.closest('.comment-item.toggle-reply-btn');
        if (!item) return;
        if (e.target.closest('.reply-box')) return;

        const existing = item.querySelector('.reply-box');
        if (existing) {
            existing.remove();
        } else {
            // 다른 곳에 열려 있는 폼은 닫기
            commentList.querySelectorAll('.reply-box').forEach(el => el.remove());
            // 템플릿 복제
            const tpl   = document.getElementById('reply-form-template').firstElementChild;
            const clone = tpl.cloneNode(true);
            clone.querySelector('input[name="parentCommentId"]').value = item.dataset.commentId;
            item.appendChild(clone);
        }
    });

    // 2) 댓글/대댓글 제출
    document.addEventListener('submit', async e => {
        if (!e.target.matches('#commentForm, #commentList .reply-box form')) return;
        e.preventDefault();

        const form     = e.target;
        const textarea = form.querySelector('textarea[name="content"]');
        const content  = textarea.value.trim();
        if (!content) {
            alert('댓글을 입력해주세요');
            return;
        }

        const parentInput     = form.querySelector('input[name="parentCommentId"]');
        const parentCommentId = parentInput?.value || null;

        try {
            const res = await fetch(`/post/${postId}/comments`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ content, parentCommentId })
            });
            if (!res.ok) throw new Error(res.statusText);
            const comment = await res.json();

            if (parentCommentId) {
                // 대댓글: renderReply 사용
                const parentEl = commentList.querySelector(
                    `.comment-item[data-comment-id="${parentCommentId}"]`
                );
                parentEl.insertAdjacentHTML('beforeend', renderReply(comment));
                form.closest('.reply-box').remove();
            } else {
                // 루트 댓글: renderComment 사용
                commentList.insertAdjacentHTML('beforeend', renderComment(comment));
            }

            textarea.value = '';
            textarea.focus();
        } catch (err) {
            console.error('댓글 등록 실패:', err);
            alert('댓글 등록 중 오류가 발생했습니다.');
        }
    });
});

// 루트 댓글 HTML (클릭하면 토글 가능)
function renderComment(c) {
    return `
    <div class="comment-item toggle-reply-btn mb-3 border rounded p-2"
         data-comment-id="${c.id}" style="cursor:pointer;">
      <div class="text-muted small mb-1 d-flex justify-content-between">
        <span>${c.nickname}</span>
        <span>${new Date(c.createDate).toLocaleString('ko-KR', {
        month:'2-digit', day:'2-digit', hour:'2-digit', minute:'2-digit',  hour12: false
    })}</span>
      </div>
      <div class="ms-3">${c.content}</div>
    </div>
  `;
}

// 대댓글 HTML (토글 불필요)
function renderReply(r) {
    return `
    <div class="ms-4 p-2 my-box mt-2 p-3"
         data-comment-id="${r.id}">
      <div class="text-muted small mb-1 d-flex justify-content-between">
        <span>${r.nickname}</span>
        <span>${new Date(r.createDate).toLocaleString('ko-KR', {
        month:'2-digit', day:'2-digit', hour:'2-digit', minute:'2-digit', hour12: false
    })}</span>
      </div>
      <div class="ms-3">${r.content}</div>
    </div>
  `;
}
