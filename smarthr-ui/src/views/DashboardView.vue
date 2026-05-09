<template>
  <div class="dashboard">
    <h2>Dashboard</h2>
    <div class="stats-grid">
      <div class="stat-card">
        <div class="stat-icon">👥</div>
        <div class="stat-content">
          <h3>{{ stats.activeCandidates }}</h3>
          <p>Active Candidates</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">💼</div>
        <div class="stat-content">
          <h3>{{ stats.openJobs }}</h3>
          <p>Open Positions</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">📅</div>
        <div class="stat-content">
          <h3>{{ stats.scheduledInterviews }}</h3>
          <p>Scheduled Interviews</p>
        </div>
      </div>
      <div class="stat-card">
        <div class="stat-icon">👔</div>
        <div class="stat-content">
          <h3>{{ stats.totalEmployees }}</h3>
          <p>Total Employees</p>
        </div>
      </div>
    </div>

    <div class="dashboard-grid">
      <div class="card pipeline-card">
        <h3>Candidate Pipeline</h3>
        <div class="pipeline-stages">
          <div v-for="stage in pipeline" :key="stage.name" class="pipeline-stage">
            <div class="stage-header">
              <span>{{ stage.name }}</span>
              <span class="stage-count">{{ stage.count }}</span>
            </div>
            <div class="stage-bar">
              <div class="stage-fill" :style="{ width: stage.percentage + '%', background: stage.color }"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="card recent-activity">
        <h3>Recent Activity</h3>
        <div class="activity-list">
          <div v-for="activity in activities" :key="activity.id" class="activity-item">
            <div class="activity-icon">{{ activity.icon }}</div>
            <div class="activity-content">
              <p>{{ activity.text }}</p>
              <span>{{ activity.time }}</span>
            </div>
          </div>
        </div>
      </div>

      <div class="card upcoming-interviews">
        <h3>Upcoming Interviews</h3>
        <div class="interview-list">
          <div v-for="interview in upcomingInterviews" :key="interview.id" class="interview-item">
            <div class="interview-time">
              <span class="time">{{ interview.time }}</span>
              <span class="date">{{ interview.date }}</span>
            </div>
            <div class="interview-info">
              <h4>{{ interview.candidate }}</h4>
              <p>{{ interview.position }}</p>
            </div>
            <span :class="['status', interview.status]">{{ interview.status }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'

const stats = ref({
  activeCandidates: 127,
  openJobs: 23,
  scheduledInterviews: 15,
  totalEmployees: 458
})

const pipeline = ref([
  { name: 'Applied', count: 45, percentage: 100, color: '#4CAF50' },
  { name: 'Screening', count: 32, percentage: 71, color: '#2196F3' },
  { name: 'Interview', count: 28, percentage: 62, color: '#FF9800' },
  { name: 'Offer', count: 12, percentage: 27, color: '#9C27B0' },
  { name: 'Hired', count: 10, percentage: 22, color: '#4CAF50' }
])

const activities = ref([
  { id: 1, icon: '✅', text: 'Sarah Chen moved to Offer stage', time: '2 hours ago' },
  { id: 2, icon: '📝', text: 'New application for Senior Developer', time: '4 hours ago' },
  { id: 3, icon: '📅', text: 'Interview scheduled with Michael Park', time: '5 hours ago' },
  { id: 4, icon: '👔', text: 'David Kim joined as Product Manager', time: '1 day ago' }
])

const upcomingInterviews = ref([
  { id: 1, time: '10:00', date: 'Today', candidate: 'Emily Watson', position: 'UX Designer', status: 'confirmed' },
  { id: 2, time: '14:00', date: 'Today', candidate: 'Alex Rivera', position: 'Backend Engineer', status: 'pending' },
  { id: 3, time: '11:00', date: 'Tomorrow', candidate: 'James Liu', position: 'Data Analyst', status: 'confirmed' }
])
</script>

<style scoped>
.dashboard h2 {
  margin-bottom: 24px;
  color: #333;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.stat-icon {
  font-size: 36px;
}

.stat-content h3 {
  font-size: 32px;
  color: #333;
}

.stat-content p {
  color: #888;
  font-size: 14px;
}

.dashboard-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.card h3 {
  margin-bottom: 20px;
  color: #333;
  font-size: 18px;
}

.pipeline-card {
  grid-column: 1;
}

.recent-activity {
  grid-column: 2;
}

.upcoming-interviews {
  grid-column: 1 / -1;
}

.pipeline-stages {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.pipeline-stage {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.stage-header {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
}

.stage-count {
  font-weight: bold;
  color: #333;
}

.stage-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.stage-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s ease;
}

.activity-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.activity-item {
  display: flex;
  gap: 12px;
  align-items: flex-start;
}

.activity-icon {
  font-size: 20px;
}

.activity-content p {
  font-size: 14px;
  color: #333;
}

.activity-content span {
  font-size: 12px;
  color: #888;
}

.interview-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.interview-item {
  display: flex;
  align-items: center;
  gap: 20px;
  padding: 16px;
  background: #f9f9f9;
  border-radius: 8px;
}

.interview-time {
  text-align: center;
  min-width: 60px;
}

.interview-time .time {
  display: block;
  font-size: 18px;
  font-weight: bold;
  color: #333;
}

.interview-time .date {
  font-size: 12px;
  color: #888;
}

.interview-info {
  flex: 1;
}

.interview-info h4 {
  font-size: 16px;
  color: #333;
}

.interview-info p {
  font-size: 14px;
  color: #888;
}

.status {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status.confirmed {
  background: #e8f5e9;
  color: #4CAF50;
}

.status.pending {
  background: #fff3e0;
  color: #FF9800;
}
</style>