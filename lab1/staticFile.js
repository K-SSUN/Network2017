// 모듈 추출
const express = require('express');
// 서버 생성
const app = express();
app.use(express.static('public'));
// request 이벤트 리스너 설정

// 1) 디폴트 홈페이지 출력
app.get('/', (req, res) => {
    res.send('index.html');
});

// 2) hello.html 출력
app.get('/hello.html', (req, res) => {
    res.send('hello.html');
});

// 3) 음악파일 출력
app.get('/audio', (req, res) => {
    res.send('audio.mp3');
});

// 4) 교재 이미지 출력
app.get('/text.png', (req, res) => {
    res.send('text.png');
});

// 5) "Not Found" 에러메시지 출력
app.get('/no.html', (req, res) => {
    res.send(404);
});

// 6) 네이버 사이트 리디렉션
app.get('/hong.html', (req, res) => {
    res.redirect('http://www.naver.com');
});

// 7) 네이버 사이트 리디렉션
app.get('/hong2.html', (req, res) => {
    res.redirect('http://ktis.kookmin.ac.kr');
});

app.get('*', (req, res) => {
    resp.send(404);
    resp.send('해당 경로에는 아무것도 없습니다.');
});
 
// 서버를 실행합니다.
app.listen(8080, () => {
    console.log('Server running at http://127.0.0.1:52273');
});