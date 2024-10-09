const categorySelect = document.getElementById('categorySelect');
const createRoomButton = document.getElementById('createRoomButton');
let chatRoomId;

// 채팅방 생성 버튼 클릭 시
createRoomButton.onclick = function() {
    console.log("문의하기 클릭됨");

    const selectedCategory = categorySelect.value;

    if (selectedCategory) {
        fetch('/api/chat/chat_room', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({ category: selectedCategory }),
        })
        .then(response => response.json())
        .then(data => {
            chatRoomId = data.id; // 생성된 채팅방 ID 저장
            document.getElementById('chat-Container').style.display = 'flex'; // chat-container 표시
            document.getElementById('chat-setup').style.display = 'none'; // chat-container 표시

            history.pushState(null, '', `/chat?chatRoomId=${chatRoomId}`);
        })
        .catch(error => {
            console.error('채팅방 생성 중 오류가 발생했습니다:', error);
        });
    } else {
        alert("카테고리를 선택해주세요.");
    }
};