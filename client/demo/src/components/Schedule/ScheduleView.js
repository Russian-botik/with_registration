import React, { useState, useEffect } from 'react';
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
} from '@mui/material';

const ScheduleView = () => {
  const [schedule, setSchedule] = useState([]);
  const [selectedTeacher, setSelectedTeacher] = useState('');
  const [teachers, setTeachers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  const timeSlots = [
    '8:30 - 10:00',
    '10:15 - 11:45',
    '12:00 - 13:30',
    '13:45 - 15:15',
    '15:30 - 17:00',
    '17:15 - 18:45',
  ];

  const days = ['Понедельник', 'Вторник', 'Среда', 'Четверг', 'Пятница'];

  useEffect(() => {
    fetchTeachers();
  }, []);

  useEffect(() => {
    if (selectedTeacher) {
      fetchSchedule();
    }
  }, [selectedTeacher]);

  const fetchTeachers = async () => {
    try {
      const response = await fetch('/api/teachers', {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
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
      const response = await fetch(`/api/schedule/teacher/${selectedTeacher}`, {
        headers: {
          'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
      });
      if (response.ok) {
        const data = await response.json();
        setSchedule(data);
      } else {
        setError('Ошибка при загрузке расписания');
      }
    } catch (err) {
      setError('Ошибка при загрузке расписания');
    }
  };

  const getScheduleCell = (day, timeSlot) => {
    const lesson = schedule.find(
      (item) => item.day === day && item.timeSlot === timeSlot
    );
    return lesson ? (
      <Box>
        <Typography variant="subtitle2">{lesson.subject}</Typography>
        <Typography variant="body2">{lesson.room}</Typography>
        <Typography variant="body2">{lesson.group}</Typography>
      </Box>
    ) : null;
  };

  if (loading) {
    return <Typography>Загрузка...</Typography>;
  }

  if (error) {
    return <Typography color="error">{error}</Typography>;
  }

  return (
    <Box sx={{ p: 3 }}>
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <FormControl fullWidth>
            <InputLabel>Преподаватель</InputLabel>
            <Select
              value={selectedTeacher}
              onChange={(e) => setSelectedTeacher(e.target.value)}
              label="Преподаватель"
            >
              {teachers.map((teacher) => (
                <MenuItem key={teacher.id} value={teacher.id}>
                  {teacher.name}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
        </Grid>
        <Grid item xs={12}>
          <TableContainer component={Paper}>
            <Table>
              <TableHead>
                <TableRow>
                  <TableCell>Время</TableCell>
                  {days.map((day) => (
                    <TableCell key={day}>{day}</TableCell>
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