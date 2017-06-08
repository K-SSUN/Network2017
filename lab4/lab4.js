// ��� ����
const express = require('express');
// ���� ����
const app = express();
app.use(express.static('public'));

app.get('/register', (request, response) => {
    // HTML ������ ���ڿ� ����
    if(request.query.id){
        id = request.query.id;
        command = request.query.command;
        list[id] = command;
        console.log(`${id}��command=${command}�� ��ϵǾ����ϴ�.`);
        response.send(`Ȩ������ ID=${id}�� command=${list[id]}�� ��ϵǾ����ϴ�.`);
    }
    else{
        let output = '';
        output += '<form method="get">';
        output += 'ID:   <input type="text" size=15 name="id" /> </br>';
        output += 'command:  <input type="text" size=80 name="command" /> </br>';
        output += '  <input type="submit" value="home page setting"/>';
        output += '</form>';
        // ����
        response.send(output);
    }
});

app.get('/:id', (request, response) => {
    // ���� ����
    id = request.params.id;
    if(id[0] == ':') id = id.substr(1, id.length-1);
    if(list[id]){
        console.log(`${id}�� ��ϵ� command=${list[id]}�� �����մϴ�.`);
        eval(list[id]);
    }
    else
        response.status(404).send(`Ȩ������ID(=${id})�� �������� �ʽ��ϴ�.`);
});

app.get('/hong.html', (request, response) => {
    response.redirect('http://naver.com');
});

app.get('/hong2.html', (request, response) => {
    response.send('<h1> Hello Hong</h1>');
});

app.get('*', (request, response) => {
    response.send(404);
    response.send('�ش� ��ο��� �ƹ��͵� �����ϴ�.');
});

// ���� ����
app.listen(8080, () => {
    console.log('Server running at http://10.30.119.78:8080');
});
