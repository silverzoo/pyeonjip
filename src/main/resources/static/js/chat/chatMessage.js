const chatBody = document.getElementById('chatBody');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');

let selectedMessageId = null;
const contextMenu = document.getElementById('contextMenu');

// WebSocket 연결
const socket = new WebSocket('ws://localhost:8080/ws/chat'); // 실제 URL로 변경필요.

// WebSocket 메시지 수신 처리
socket.onmessage = function(event) {
    const message = JSON.parse(event.data); // JSON 형태로 수신

    // message.id가 있는지 확인
    if (message && message.text && message.id) {
        displayMessage(message.text, message.id, false); // 수신된 메시지 표시 (상대방 메시지)
    }
};

// 메시지 표시 함수
function displayMessage(message, messageId, sent = false) {
    if (!message) return;

    const messageElement = document.createElement('div');
    messageElement.classList.add('message');
    messageElement.textContent = message; // 수신한 메시지 내용을 추가
    messageElement.dataset.messageId = messageId; // 메시지 ID 저장 (수정 시 사용)

    if (sent) {
        messageElement.classList.add('sent'); // 내가 보낸 메시지
    } else {
        messageElement.classList.add('received'); // 상대방이 보낸 메시지
    }

    chatBody.appendChild(messageElement); // 채팅 본문에 추가
    chatBody.scrollTop = chatBody.scrollHeight; // 스크롤을 가장 아래로 이동
}

// 메시지 요소에 우클릭 이벤트 추가
chatBody.addEventListener('contextmenu', function(event) {
    event.preventDefault(); // 기본 우클릭 메뉴를 막음

    // 클릭한 메시지 요소를 찾기
    const messageElement = event.target.closest('.message.sent'); // sent 메시지만 선택
    if (messageElement) {
        selectedMessageId = messageElement.dataset.messageId; // 메시지 ID 저장

        // 우클릭 메뉴 위치 설정
        contextMenu.style.top = `${event.clientY}px`;
        contextMenu.style.left = `${event.clientX}px`;
        contextMenu.style.display = 'block';
    }
});

// 컨텍스트 메뉴 숨기기
document.addEventListener('click', function() {
    contextMenu.style.display = 'none';
});

// "메시지 수정" 클릭 시 이벤트 처리
document.getElementById('editMessage').addEventListener('click', function() {
    if (selectedMessageId) {
        const newMessage = prompt("수정할 메시지를 입력하세요:");
        if (newMessage) {
            // 서버로 수정된 메시지 전송
            fetch(`/api/chat/message/${selectedMessageId}`, {
                method: 'PUT',
                headers: {
                    'Content-Type': 'text/plain', // 변경된 타입
                },
                body: newMessage, // 문자열로 직접 전송
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('메시지 수정 실패');
                }
                return response.json();
            })
            .then(updatedMessage => {
                const messageElement = document.querySelector(`[data-message-id="${selectedMessageId}"]`);
                if (messageElement) {
                    messageElement.textContent = updatedMessage.message; // 수정된 메시지로 업데이트
                }
            })
            .catch(error => {
                console.error('메시지 수정 중 오류 발생:', error);
            });
        }
    }
});


// 전송 버튼 클릭 시 메시지 전송
sendButton.onclick = function() {
    const message = messageInput.value.trim(); // 메시지 앞뒤 공백 제거
    if (message) {
        // 메시지를 보내고 받은 ID를 displayMessage에 전달
        displayMessage(message, null, true); // 내 메시지를 화면에 표시

        // 서버에 메시지 전송
        fetch(`/api/chat/message?chatRoomId=${chatRoomId}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'text/plain', // 메시지 형식 설정
            },
            body: message, // 메시지 본문
        })
        .then(response => {
            if (!response.ok) {
                throw new Error('네트워크 응답이 좋지 않습니다.');
            }
            return response.json(); // 응답을 JSON 형태로 변환
        })
        .then(data => {
            createdMessageId = data.id; // 서버에서 생성된 메시지 ID를 저장
            // 생성된 메시지 ID를 사용하여 자신의 메시지 업데이트
            const messageElement = document.querySelector(`[data-message-id="${null}"]`);
            if (messageElement) {
                messageElement.dataset.messageId = createdMessageId; // 데이터 속성 업데이트
            }
            messageInput.value = ''; // 입력 필드 초기화
        })
        .catch(error => {
            console.error('문제가 발생했습니다:', error);
        });
    }
};

