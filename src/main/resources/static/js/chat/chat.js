const chatBody = document.getElementById('chatBody');
const messageInput = document.getElementById('messageInput');
const sendButton = document.getElementById('sendButton');

// WebSocket 연결
const socket = new WebSocket('ws://localhost:8080/ws/chat'); // 실제 URL로 변경하세요.

// WebSocket 메시지 수신 처리
socket.onmessage = function(event) {
    const message = JSON.parse(event.data); // JSON 형태로 수신
    displayMessage(message); // 메시지 표시 함수 호출
};

// 메시지 표시 함수
function displayMessage(message) {
    const messageElement = document.createElement('div');
    messageElement.textContent = message.text; // 수신한 메시지 내용을 추가
    chatBody.appendChild(messageElement); // 채팅 본문에 추가
    chatBody.scrollTop = chatBody.scrollHeight; // 스크롤을 가장 아래로 이동
}

// 전송 버튼 클릭 시 메시지 전송
sendButton.onclick = function() {
    const message = messageInput.value;
    if (message) {
        socket.send(JSON.stringify({ text: message })); // 메시지 전송
        messageInput.value = ''; // 입력 필드 초기화
    }
};
