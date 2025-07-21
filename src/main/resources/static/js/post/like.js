document.addEventListener('DOMContentLoaded', ()=>{
    const btn = document.getElementById('likeBtn');
    const icon = document.getElementById('likeIcon');
    const likeCount = document.getElementById('likeCount');

    btn.addEventListener("click", async () => {
        try{
            const postId = btn.getAttribute("data-post-id");
            const response = await fetch(`/post/${postId}/like`, {
                method: 'POST',
                headers: {
                    'Content-Type' : 'application/json'
                }
            });
            if(response.status == 401){
                alert("로그인 후 이용할 수 있습니다.");
                return;
            }
            if(!response.ok) throw new Error(response.statusText);
            const data = await response.json();
            const hasLiked = data?.hasLiked;
            const cnt = data?.likeCount;
            if(hasLiked){
                icon.classList.replace('bi-hand-thumbs-up', 'bi-hand-thumbs-up-fill');
            } else{
                icon.classList.replace('bi-hand-thumbs-up-fill', 'bi-hand-thumbs-up')
            }
            likeCount.textContent = cnt;
        } catch (e) {
            console.error('좋아요 처리 실패:', e);
            alert('좋아요를 처리하는 데 문제가 발생했습니다.');
        }
    })
})