const axios = require('axios');

const API_URL = 'http://localhost:8082';

// Функция для ожидания
const sleep = (ms) => new Promise(resolve => setTimeout(resolve, ms));

// Тесты аутентификации
async function testAuth() {
    try {
        console.log('Testing Auth API...');
        
        // Регистрация
        const registerResponse = await axios.post(`${API_URL}/api/auth/register`, {
            username: 'testuser',
            password: 'password123',
            email: 'test@example.com'
        });
        console.log('Register Response:', registerResponse.data);

        // Вход
        const loginResponse = await axios.post(`${API_URL}/api/auth/login`, {
            username: 'testuser',
            password: 'password123'
        });
        console.log('Login Response:', loginResponse.data);
        
        return loginResponse.data;
    } catch (error) {
        console.error('Auth Test Error:', error.response?.data || error.message);
        throw error;
    }
}

// Тесты аудиторий
async function testClassrooms() {
    try {
        console.log('\nTesting Classrooms API...');
        
        // Добавление аудитории
        const addResponse = await axios.post(`${API_URL}/api/classrooms`, {
            name: '101',
            capacity: 30,
            type: 'LECTURE'
        });
        console.log('Add Classroom Response:', addResponse.data);

        // Получение списка аудиторий
        const listResponse = await axios.get(`${API_URL}/api/classrooms`);
        console.log('Get Classrooms Response:', listResponse.data);
        
        return addResponse.data;
    } catch (error) {
        console.error('Classrooms Test Error:', error.response?.data || error.message);
        throw error;
    }
}

// Тесты предметов
async function testSubjects() {
    try {
        console.log('\nTesting Subjects API...');
        
        // Добавление предмета
        const addResponse = await axios.post(`${API_URL}/api/subjects`, {
            name: 'Математика',
            description: 'Базовый курс математики'
        });
        console.log('Add Subject Response:', addResponse.data);

        // Получение списка предметов
        const listResponse = await axios.get(`${API_URL}/api/subjects`);
        console.log('Get Subjects Response:', listResponse.data);
        
        return addResponse.data;
    } catch (error) {
        console.error('Subjects Test Error:', error.response?.data || error.message);
        throw error;
    }
}

// Тесты преподавателей
async function testTeachers() {
    try {
        console.log('\nTesting Teachers API...');
        
        // Добавление преподавателя
        const addResponse = await axios.post(`${API_URL}/api/teachers`, {
            firstName: 'Иван',
            lastName: 'Иванов',
            email: 'ivanov@example.com'
        });
        console.log('Add Teacher Response:', addResponse.data);

        // Получение списка преподавателей
        const listResponse = await axios.get(`${API_URL}/api/teachers`);
        console.log('Get Teachers Response:', listResponse.data);
        
        return addResponse.data;
    } catch (error) {
        console.error('Teachers Test Error:', error.response?.data || error.message);
        throw error;
    }
}

// Тесты связей преподавателей с предметами
async function testTeacherSubjects(teacherId, subjectId) {
    try {
        console.log('\nTesting TeacherSubjects API...');
        
        // Добавление связи
        const addResponse = await axios.post(`${API_URL}/api/teacher-subjects`, {
            teacherId: teacherId,
            subjectId: subjectId
        });
        console.log('Add TeacherSubject Response:', addResponse.data);

        // Получение связей преподавателя
        const listResponse = await axios.get(`${API_URL}/api/teacher-subjects/teacher/${teacherId}`);
        console.log('Get TeacherSubjects Response:', listResponse.data);
        
        return addResponse.data;
    } catch (error) {
        console.error('TeacherSubjects Test Error:', error.response?.data || error.message);
        throw error;
    }
}

// Тесты расписания
async function testSchedules(teacherSubjectId, classroomId) {
    try {
        console.log('\nTesting Schedules API...');
        
        // Добавление занятия
        const addResponse = await axios.post(`${API_URL}/api/schedules`, {
            teacherSubjectId: teacherSubjectId,
            classroomId: classroomId,
            dayOfWeek: 'MONDAY',
            startTime: '09:00',
            endTime: '10:30'
        });
        console.log('Add Schedule Response:', addResponse.data);

        // Получение расписания
        const listResponse = await axios.get(`${API_URL}/api/schedules`);
        console.log('Get Schedules Response:', listResponse.data);
        
        return addResponse.data;
    } catch (error) {
        console.error('Schedules Test Error:', error.response?.data || error.message);
        throw error;
    }
}

// Основная функция запуска тестов
async function runTests() {
    try {
        console.log('Starting API tests...');
        
        // Ждем запуска приложения
        await sleep(5000);
        
        // Запускаем тесты последовательно
        await testAuth();
        const classroom = await testClassrooms();
        const subject = await testSubjects();
        const teacher = await testTeachers();
        const teacherSubject = await testTeacherSubjects(teacher.id, subject.id);
        await testSchedules(teacherSubject.id, classroom.id);
        
        console.log('\nAll tests completed successfully!');
    } catch (error) {
        console.error('\nTest execution failed:', error);
        process.exit(1);
    }
}

// Запускаем тесты
runTests(); 