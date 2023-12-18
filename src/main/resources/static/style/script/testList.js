const testList=document.getElementById('test-list');

const testItem =document.createElement('div');
testItem.className='pannel';

const testName=document.createElement('div');
testName.innerText='test name';//название теста
testName.className='test-name';


const testReward=document.createElement('div');
testReward.title='Max reward';//не менять это
testReward.innerText='17 LC'; //кол-во вопросов в тесте( макс. кол-во баллов которое можно получить) )
testReward.className='test-reward';

testList.appendChild(testItem);
testItem.appendChild(testName);
testItem.appendChild(testReward);