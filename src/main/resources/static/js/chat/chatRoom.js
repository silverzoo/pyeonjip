const categorySelect = document.getElementById('categorySelect');
const createRoomButton = document.getElementById('createRoomButton');
let chatRoomId;

function formatDate(dateString) {
    const date = new Date(dateString);

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    return `${year}-${month}-${day} ${hours}:${minutes}`;
}

// 이전 채팅방 목록을 화면에 나열하는 함수
function displayChatRooms(chatRooms) {
    const chatBody = document.getElementById('chatBody');
    chatBody.innerHTML = ''; // 기존 내용을 초기화

    if (chatRooms.length === 0) {
        chatBody.innerHTML = '<p>이전 문의 내역이 없습니다.</p>';
    } else {
        chatRooms.forEach(room => {
            console.log(room)
            const roomElement = document.createElement('div');
            roomElement.classList.add('chat-room');

            const formattedDate = formatDate(room.createdAt); // 날짜 포맷 적용
            roomElement.innerHTML = `<p>문의 일시: ${formattedDate}, 문의사항: ${room.category}</p>`

            roomElement.onclick = function() {
                // 클릭 시 해당 채팅방으로 이동
                history.pushState(null, '', `/chat?chatRoomId=${room.id}`);
                document.getElementById('chat-Container').style.display = 'flex'; // chat-container 표시
                document.getElementById('chat-setup').style.display = 'none'; // chat-setup 숨기기

                // 선택한 채팅방의 메시지 로드
                loadChatMessages(room.id);
            };
            chatBody.appendChild(roomElement); // 채팅 목록에 추가
        });
    }
}


// 채팅 내역을 화면에 표시하는 함수
function displayMessages(messages) {
    const chatBody = document.getElementById('chatBody');
    chatBody.innerHTML = ''; // 기존 메시지 초기화

    messages.forEach(message => {
        const messageElement = document.createElement('div');
        messageElement.classList.add('chat-message');

        // 메시지가 JSON 형식인지 확인 후 파싱
        let messageContent;
        try {
            const parsedMessage = JSON.parse(message.message);
            messageContent = parsedMessage.message; // JSON 형식이라면 이 필드 사용
        } catch (error) {
            messageContent = message.message; // JSON 형식이 아니면 원본 메시지 사용
        }

        messageElement.textContent = `${message.senderEmail}: ${messageContent}`;
        chatBody.appendChild(messageElement);
    });
}


// 채팅방 생성 버튼 클릭 시
createRoomButton.onclick = function() {
    console.log("문의하기 클릭됨");
    if(categorySelect.value === '이전 문의 내역'){
        const userId = 2; // 실제 사용자 ID로 변경 필요

        // 이전 채팅방 내역 가져오기
        fetch(`/api/chat/chat-room-list/${userId}`, {
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
            fetch('/api/chat/chat-room', {
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

// 채팅방 클릭 시 메시지 불러오기
function loadChatMessages(chatRoomId) {
    fetch(`/api/chat/chat-message-history/${chatRoomId}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json',
        },
    })
    .then(response => {
        if (!response.ok) {
            throw new Error('채팅 메시지를 불러오는 중 오류가 발생했습니다.');
        }
        return response.json();
    })
    .then(chatMessages => {
        console.log(chatMessages);
        const chatBody = document.getElementById('chatBody');
        chatBody.innerHTML = ''; // 기존 채팅 내용 초기화

        if (chatMessages.length === 0) {
            chatBody.innerHTML = '<p>이전 메시지가 없습니다.</p>';
        } else {
            chatMessages.forEach(message => {
                const messageElement = document.createElement('div');
                messageElement.classList.add('chat-message');

                // 메시지가 JSON 형식인지 확인 후 파싱
                let messageContent;
                try {
                    const parsedMessage = JSON.parse(message.message);
                    messageContent = parsedMessage.message; // JSON 형식이라면 이 필드 사용
                } catch (error) {
                    messageContent = message.message; // JSON 형식이 아니면 원본 메시지 사용
                }

                messageElement.textContent = `${message.senderEmail}: ${messageContent}`;
                chatBody.appendChild(messageElement); // 채팅 메시지 목록에 추가
            });
        }
    })
    .catch(error => {
        console.error('채팅 메시지 불러오기 중 오류가 발생했습니다:', error);
    });
}