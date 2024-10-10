const categorySelect = document.getElementById('categorySelect');
const createRoomButton = document.getElementById('createRoomButton');
let chatRoomId;

// 이전 채팅방 목록을 화면에 나열하는 함수
function displayChatRooms(chatRooms) {
    const chatBody = document.getElementById('chatBody');
    chatBody.innerHTML = ''; // 기존 내용을 초기화

    if (chatRooms.length === 0) {
        chatBody.innerHTML = '<p>이전 문의 내역이 없습니다.</p>';
    } else {
        chatRooms.forEach(room => {
            const roomElement = document.createElement('div');
            roomElement.classList.add('chat-room');
            roomElement.textContent = `채팅방 ID: ${room.id}, 카테고리: ${room.category}`;
            roomElement.onclick = function() {
                // 클릭 시 해당 채팅방으로 이동
                history.pushState(null, '', `/chat?chatRoomId=${room.id}`);
                document.getElementById('chat-Container').style.display = 'flex'; // chat-container 표시
                document.getElementById('chat-setup').style.display = 'none'; // chat-setup 숨기기
            };
            chatBody.appendChild(roomElement); // 채팅 목록에 추가
        });
    }
}

// 채팅방 생성 버튼 클릭 시
createRoomButton.onclick = function() {
    console.log("문의하기 클릭됨");
    if(categorySelect.value === 'inquiry_previous'){
        const userId = 2; // 실제 사용자 ID로 변경 필요

        // 이전 채팅방 내역 가져오기
        fetch(`/api/chat/chat_room_list/${userId}`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json',
            },
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('이전 문의 내역을 가져오는 중 오류가 발생했습니다.');
            }
            return response.json();
        })
        .then(chatRooms => {
            // 이전 문의 내역을 가져왔을 때도 채팅 UI로 전환
            document.getElementById('chat-Container').style.display = 'flex'; // chat-container 표시
            document.getElementById('chat-setup').style.display = 'none'; // chat-setup 숨기기

            // 채팅방 목록을 화면에 나열
            displayChatRooms(chatRooms);
        })
        .catch(error => {
            console.error('채팅방 내역 가져오기 중 오류가 발생했습니다:', error);
        });
    } else {
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
                document.getElementById('chat-setup').style.display = 'none'; // chat-container 숨기기

                history.pushState(null, '', `/chat?chatRoomId=${chatRoomId}`);
            })
            .catch(error => {
                console.error('채팅방 생성 중 오류가 발생했습니다:', error);
            });
        } else {
            alert("카테고리를 선택해주세요.");
        }
    }
};