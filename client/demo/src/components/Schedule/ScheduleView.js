import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Box,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography,
  Select,
  MenuItem,
  FormControl,
  InputLabel,
  Grid,
  Button,
  Alert,
} from '@mui/material';
import { API_BASE_URL } from '../../config';

const ScheduleView = () => {
  const navigate = useNavigate();
  const [schedule, setSchedule] = useState([]);
  const [teachers, setTeachers] = useState([]);
  const [selectedTeacher, setSelectedTeacher] = useState('');
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [successMessage, setSuccessMessage] = useState('');

  const timeSlots = [
    '9:00 - 10:30',
    '10:30 - 12:00',
    '12:00 - 13:30',
    '13:30 - 15:00',
    '15:00 - 16:30',
    '16:30 - 18:00'
  ];

  const days = ['MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY'];
  const dayNames = {
    'MONDAY': 'Понедельник',
    'TUESDAY': 'Вторник',
    'WEDNESDAY': 'Среда',
    'THURSDAY': 'Четверг',
    'FRIDAY': 'Пятница'
  };

  useEffect(() => {
    const token = localStorage.getItem('token');
    if (!token) {
      navigate('/login');
      return;
    }
    fetchTeachers();
  }, [navigate]);

  useEffect(() => {
    if (selectedTeacher) {
      fetchSchedule();
    }
  }, [selectedTeacher]);

  const fetchTeachers = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/teachers`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.status === 401) {
        localStorage.removeItem('token');
        navigate('/login');
        return;
      }

      if (response.ok) {
        const data = await response.json();
        setTeachers(data);
      } else {
        setError('Ошибка при загрузке списка преподавателей');
      }
    } catch (err) {
      setError('Ошибка при загрузке списка преподавателей');
    } finally {
      setLoading(false);
    }
  };

  const fetchSchedule = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      const response = await fetch(`${API_BASE_URL}/api/schedules/all`, {
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (response.status === 401) {
        localStorage.removeItem('token');
        navigate('/login');
        return;
      }

      if (response.ok) {
        const data = await response.json();
        // Фильтруем расписание по выбранному преподавателю (приводим к строке)
        const filteredSchedule = selectedTeacher 
          ? data.filter(schedule => 
              String(schedule.teacherSubject?.teacher?.id) === String(selectedTeacher))
          : data;
        setSchedule(filteredSchedule);
      } else {
        setError('Ошибка при загрузке расписания');
      }
    } catch (err) {
      setError('Ошибка при загрузке расписания');
    }
  };

  const generateSchedule = async () => {
    try {
      const token = localStorage.getItem('token');
      if (!token) {
        navigate('/login');
        return;
      }

      // Сначала генерируем связи преподаватель-предмет
      const teacherSubjectResponse = await fetch(`${API_BASE_URL}/api/generator/teacher-subjects`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (teacherSubjectResponse.status === 401) {
        localStorage.removeItem('token');
        navigate('/login');
        return;
      }

      if (!teacherSubjectResponse.ok) {
        throw new Error('Ошибка при генерации связей преподаватель-предмет');
      }

      // Затем генерируем расписание
      const scheduleResponse = await fetch(`${API_BASE_URL}/api/generator/schedule`, {
        method: 'POST',
        headers: {
          'Authorization': `Bearer ${token}`,
          'Content-Type': 'application/json'
        }
      });

      if (scheduleResponse.status === 401) {
        localStorage.removeItem('token');
        navigate('/login');
        return;
      }

      if (!scheduleResponse.ok) {
        throw new Error('Ошибка при генерации расписания');
      }

      setSuccessMessage('Расписание успешно сгенерировано');
      fetchSchedule();
    } catch (err) {
      setError(err.message);
    }
  };

  const getScheduleCell = (day, timeSlot) => {
    const [startTime] = timeSlot.split(' - ');
    const lesson = schedule.find(
      (item) => item.dayOfWeek === day && item.startTime && item.startTime.startsWith(startTime)
    );

    if (!lesson) return null;

    return (
      <Box>
        <Typography variant="subtitle2" color="primary">
          {lesson.teacherSubject?.subject?.name || 'Предмет'}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          Ауд. {lesson.classroom?.name || 'Аудитория'}
        </Typography>
        <Typography variant="body2" color="text.secondary">
          {lesson.teacherSubject?.teacher?.lastName} {lesson.teacherSubject?.teacher?.firstName}
        </Typography>
      </Box>
    );
  };

  if (loading) {
    return <Typography>Загрузка...</Typography>;
  }

  return (
    <Box sx={{ p: 3 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 2 }}>
            <FormControl sx={{ minWidth: 200 }}>
              <InputLabel>Преподаватель</InputLabel>
              <Select
                value={selectedTeacher}
                onChange={(e) => setSelectedTeacher(e.target.value)}
                label="Преподаватель"
              >
                {teachers.map((teacher) => (
                  <MenuItem key={teacher.id} value={teacher.id}>
                    {teacher.lastName} {teacher.firstName}
                  </MenuItem>
                ))}
              </Select>
            </FormControl>
            <Button 
              variant="contained" 
              color="primary" 
              onClick={generateSchedule}
            >
              Сгенерировать расписание
            </Button>
          </Box>
        </Grid>

        {error && (
          <Grid item xs={12}>
            <Alert severity="error">{error}</Alert>
          </Grid>
        )}

        {successMessage && (
          <Grid item xs={12}>
            <Alert severity="success">{successMessage}</Alert>
          </Grid>
        )}

        <Grid item xs={12}>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Время</TableCell>
                  {days.map((day) => (
                    <TableCell key={day}>{dayNames[day]}</TableCell>
                  ))}
                </TableRow>
              </TableHead>
              <TableBody>
                {timeSlots.map((timeSlot) => (
                  <TableRow key={timeSlot}>
                    <TableCell>{timeSlot}</TableCell>
                    {days.map((day) => (
                      <TableCell key={`${day}-${timeSlot}`}>
                        {getScheduleCell(day, timeSlot)}
                      </TableCell>
                    ))}
                  </TableRow>
                ))}
              </TableBody>
            </Table>
          </TableContainer>
        </Grid>
      </Grid>
    </Box>
  );
};

export default ScheduleView; 