// 모듈 추출
const express = require('express');
// 서버 생성
const app = express();
app.use(express.static('public'));

app.get('/register', (request, response) => {
    // HTML 형식의 문자열 생성
    if(request.query.id){
        id = request.query.id;
        command = request.query.command;
        list[id] = command;
        console.log(`${id}에command=${command}가 등록되었습니다.`);
        response.send(`홈페이지 ID=${id}에 command=${list[id]}가 등록되었습니다.`);
    }
    else{
        let output = '';
        output += '<form method="get">';
        output += 'ID:   <input type="text" size=15 name="id" /> </br>';
        output += 'command:  <input type="text" size=80 name="command" /> </br>';
        output += '  <input type="submit" value="home page setting"/>';
        output += '</form>';
        // 응답
        response.send(output);
    }
});

app.get('/:id', (request, response) => {
    // 변수 선언
    id = request.params.id;
    if(id[0] == ':') id = id.substr(1, id.length-1);
    if(list[id]){
        console.log(`${id}에 등록된 command=${list[id]}를 실행합니다.`);
        eval(list[id]);
    }
    else
        response.status(404).send(`홈페이지ID(=${id})가 존재하지 않습니다.`);
});

app.get('/hong.html', (request, response) => {
    response.redirect('http://naver.com');
});

app.get('/hong2.html', (request, response) => {
    response.send('<h1> Hello Hong</h1>');
});

app.get('*', (request, response) => {
    response.send(404);
    response.send('해당 경로에는 아무것도 없습니다.');
});

// 서버 실행
app.listen(8080, () => {
    console.log('Server running at http://10.30.119.78:8080');
});
