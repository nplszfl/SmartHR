<template>
  <div class="interviews-page">
    <div class="page-header">
      <h2>Interview Scheduler</h2>
      <button class="btn-primary">+ Schedule Interview</button>
    </div>

    <div class="interviews-layout">
      <div class="calendar-section">
        <div class="calendar-header">
          <button class="nav-btn" @click="prevMonth">&lt;</button>
          <h3>{{ currentMonth }}</h3>
          <button class="nav-btn" @click="nextMonth">&gt;</button>
        </div>
        <div class="calendar-grid">
          <div class="day-header" v-for="day in ['Sun', 'Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat']" :key="day">
            {{ day }}
          </div>
          <div 
            v-for="date in calendarDates" 
            :key="date.day" 
            :class="['calendar-day', { 'other-month': !date.currentMonth, 'today': date.isToday, 'selected': selectedDate === date.day }]"
            @click="selectDate(date.day)"
          >
            <span class="day-number">{{ date.day }}</span>
            <div v-if="date.interviews" class="day-interviews">
              <div v-for="int in date.interviews.slice(0, 2)" :key="int.id" class="mini-interview" :class="int.type"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="schedule-section">
        <div class="schedule-header">
          <h3>{{ selectedDate ? formatSelectedDate() : 'Select a date' }}</h3>
        </div>
        
        <div class="schedule-list">
          <div v-for="interview in dayInterviews" :key="interview.id" class="interview-card">
            <div class="time-column">
              <span class="time">{{ interview.time }}</span>
              <span class="duration">{{ interview.duration }}min</span>
            </div>
            <div class="interview-content">
              <div class="interview-type" :class="interview.type">{{ interview.type }}</div>
              <h4>{{ interview.candidateName }}</h4>
              <p>{{ interview.position }}</p>
              <div class="interview-meta">
                <span>👤 {{ interview.interviewer }}</span>
                <span>📍 {{ interview.location }}</span>
              </div>
            </div>
            <div class="interview-status">
              <span :class="['status-badge', interview.status]">{{ interview.status }}</span>
              <div class="interview-actions">
                <button class="action-btn" title="Reschedule">📅</button>
                <button class="action-btn" title="Cancel">✕</button>
              </div>
            </div>
          </div>

          <div v-if="dayInterviews.length === 0" class="no-interviews">
            <p>No interviews scheduled for this day</p>
            <button class="btn-primary btn-small">Schedule Interview</button>
          </div>
        </div>
      </div>
    </div>

    <div class="upcoming-section">
      <h3>Upcoming This Week</h3>
      <div class="upcoming-list">
        <div v-for="interview in weekInterviews" :key="interview.id" class="upcoming-card">
          <div class="date-box">
            <span class="day">{{ interview.day }}</span>
            <span class="month">{{ interview.month }}</span>
          </div>
          <div class="upcoming-info">
            <h4>{{ interview.candidateName }}</h4>
            <p>{{ interview.position }} • {{ interview.time }}</p>
          </div>
          <span :class="['type-badge', interview.type]">{{ interview.type }}</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const currentDate = new Date()
const currentMonth = ref(currentDate.toLocaleDateString('en-US', { month: 'long', year: 'numeric' }))
const selectedDate = ref(9)

const calendarDates = computed(() => {
  const dates = []
  // Generate calendar dates
  for (let i = 1; i <= 35; i++) {
    const dayInterviews = []
    if ([9, 12, 15].includes(i)) {
      dayInterviews.push({ id: 1, type: 'technical' })
    }
    if ([9, 14].includes(i)) {
      dayInterviews.push({ id: 2, type: 'hr' })
    }
    dates.push({
      day: i > 31 ? i - 31 : i,
      currentMonth: i >= 1 && i <= 30,
      isToday: i === 9,
      interviews: dayInterviews.length ? dayInterviews : null
    })
  }
  return dates
})

const dayInterviews = computed(() => {
  if (selectedDate.value === 9) {
    return [
      { id: 1, time: '10:00 AM', duration: 60, type: 'technical', candidateName: 'Emily Watson', position: 'UX Designer', interviewer: 'John Smith', location: 'Room A', status: 'confirmed' },
      { id: 2, time: '2:00 PM', duration: 45, type: 'hr', candidateName: 'Alex Rivera', position: 'Backend Engineer', interviewer: 'Lisa Brown', location: 'Video Call', status: 'pending' }
    ]
  }
  if (selectedDate.value === 12) {
    return [
      { id: 3, time: '11:00 AM', duration: 60, type: 'technical', candidateName: 'James Liu', position: 'Data Analyst', interviewer: 'Mike Chen', location: 'Room B', status: 'confirmed' }
    ]
  }
  return []
})

const weekInterviews = ref([
  { id: 1, day: '09', month: 'May', candidateName: 'Emily Watson', position: 'UX Designer', time: '10:00 AM', type: 'technical' },
  { id: 2, day: '09', month: 'May', candidateName: 'Alex Rivera', position: 'Backend Engineer', time: '2:00 PM', type: 'hr' },
  { id: 3, day: '12', month: 'May', candidateName: 'James Liu', position: 'Data Analyst', time: '11:00 AM', type: 'technical' },
  { id: 4, day: '14', month: 'May', candidateName: 'Maria Garcia', position: 'Product Manager', time: '3:00 PM', type: 'panel' },
  { id: 5, day: '15', month: 'May', candidateName: 'Kevin Park', position: 'Frontend Developer', time: '10:30 AM', type: 'technical' }
])

const selectDate = (day) => {
  selectedDate.value = day
}

const formatSelectedDate = () => {
  return `May ${selectedDate.value}, 2026`
}

const prevMonth = () => {}
const nextMonth = () => {}
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.btn-primary {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  cursor: pointer;
}

.interviews-layout {
  display: grid;
  grid-template-columns: 1fr 1.5fr;
  gap: 24px;
  margin-bottom: 30px;
}

.calendar-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.calendar-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.calendar-header h3 {
  font-size: 18px;
  color: #333;
}

.nav-btn {
  background: none;
  border: 1px solid #e0e0e0;
  width: 36px;
  height: 36px;
  border-radius: 8px;
  cursor: pointer;
}

.calendar-grid {
  display: grid;
  grid-template-columns: repeat(7, 1fr);
  gap: 4px;
}

.day-header {
  text-align: center;
  padding: 8px;
  font-size: 12px;
  color: #888;
  font-weight: 500;
}

.calendar-day {
  aspect-ratio: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border-radius: 8px;
  cursor: pointer;
  position: relative;
  font-size: 13px;
  color: #333;
}

.calendar-day:hover {
  background: #f5f5f5;
}

.calendar-day.other-month {
  color: #ccc;
}

.calendar-day.today {
  background: #e3f2fd;
  color: #2196F3;
  font-weight: bold;
}

.calendar-day.selected {
  background: #4CAF50;
  color: white;
}

.day-interviews {
  display: flex;
  gap: 2px;
  position: absolute;
  bottom: 4px;
}

.mini-interview {
  width: 6px;
  height: 6px;
  border-radius: 50%;
}

.mini-interview.technical { background: #2196F3; }
.mini-interview.hr { background: #4CAF50; }
.mini-interview.panel { background: #9C27B0; }

.schedule-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.schedule-header {
  margin-bottom: 20px;
}

.schedule-header h3 {
  font-size: 18px;
  color: #333;
}

.schedule-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.interview-card {
  display: flex;
  gap: 20px;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 12px;
}

.time-column {
  display: flex;
  flex-direction: column;
  align-items: center;
  min-width: 70px;
}

.time-column .time {
  font-size: 16px;
  font-weight: bold;
  color: #333;
}

.time-column .duration {
  font-size: 12px;
  color: #888;
}

.interview-content {
  flex: 1;
}

.interview-type {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 11px;
  font-weight: 500;
  margin-bottom: 8px;
}

.interview-type.technical { background: #e3f2fd; color: #2196F3; }
.interview-type.hr { background: #e8f5e9; color: #4CAF50; }
.interview-type.panel { background: #f3e5f5; color: #9C27B0; }

.interview-content h4 {
  font-size: 16px;
  color: #333;
  margin-bottom: 4px;
}

.interview-content p {
  font-size: 14px;
  color: #666;
  margin-bottom: 8px;
}

.interview-meta {
  display: flex;
  gap: 16px;
  font-size: 12px;
  color: #888;
}

.interview-status {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  gap: 8px;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
}

.status-badge.confirmed { background: #e8f5e9; color: #4CAF50; }
.status-badge.pending { background: #fff3e0; color: #FF9800; }

.action-btn {
  background: none;
  border: 1px solid #e0e0e0;
  width: 32px;
  height: 32px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
}

.no-interviews {
  text-align: center;
  padding: 40px;
  color: #888;
}

.btn-small {
  padding: 10px 20px;
  background: #4CAF50;
  color: white;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  margin-top: 16px;
}

.upcoming-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.upcoming-section h3 {
  margin-bottom: 20px;
  color: #333;
}

.upcoming-list {
  display: flex;
  gap: 16px;
  overflow-x: auto;
}

.upcoming-card {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 10px;
  min-width: 280px;
}

.date-box {
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px 16px;
  background: white;
  border-radius: 8px;
}

.date-box .day {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.date-box .month {
  font-size: 12px;
  color: #888;
}

.upcoming-info {
  flex: 1;
}

.upcoming-info h4 {
  font-size: 14px;
  color: #333;
  margin-bottom: 4px;
}

.upcoming-info p {
  font-size: 12px;
  color: #666;
}

.type-badge {
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 11px;
  font-weight: 500;
}

.type-badge.technical { background: #e3f2fd; color: #2196F3; }
.type-badge.hr { background: #e8f5e9; color: #4CAF50; }
.type-badge.panel { background: #f3e5f5; color: #9C27B0; }
</style>