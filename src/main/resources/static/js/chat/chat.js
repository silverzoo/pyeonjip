const chatBody = document.getElementById('chatBody');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');

// WebSocket 연결
const socket = new WebSocket('ws://localhost:8080/ws/chat'); // 실제 URL로 변경필요.

// WebSocket 메시지 수신 처리
socket.onmessage = function(event) {
    const message = JSON.parse(event.data); // JSON 형태로 수신

    if (message && message.text) {
        displayMessage(message.text, false); // 수신된 메시지 표시 (상대방 메시지)
    }
};

// 메시지 표시 함수
function displayMessage(message, sent = false) {
    if (!message) return;

    const messageElement = document.createElement('div');
    messageElement.classList.add('message');

    if (sent) {
        messageElement.classList.add('sent'); // 내가 보낸 메시지
    } else {
        messageElement.classList.add('received'); // 상대방이 보낸 메시지
    }

    messageElement.textContent = message; // 수신한 메시지 내용을 추가
    chatBody.appendChild(messageElement); // 채팅 본문에 추가
    chatBody.scrollTop = chatBody.scrollHeight; // 스크롤을 가장 아래로 이동
}

// 전송 버튼 클릭 시 메시지 전송
sendButton.onclick = function() {
    const message = messageInput.value.trim(); // 메시지 앞뒤 공백 제거
    if (message) {
        displayMessage(message, true);

        socket.send(JSON.stringify({ text: message })); // 메시지 전송

        fetch(`/message?chatRoomId=${chatRoomId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({ text: message }), // 메시지를 JSON 형태로 변환하여 전송
            })
            .then(response => {
                if (!response.ok) {
                    throw new Error('네트워크 응답이 좋지 않습니다.');
                }
                messageInput.value = ''; // 입력 필드 초기화
            })
            .catch(error => {
                console.error('문제가 발생했습니다:', error);
            });
    }
};
