<template>
  <div class="candidates-page">
    <div class="page-header">
      <h2>Candidate Pipeline</h2>
      <button class="btn-primary">+ Add Candidate</button>
    </div>

    <div class="pipeline-overview">
      <div v-for="stage in stages" :key="stage.id" class="pipeline-column">
        <div class="column-header">
          <h3>{{ stage.name }}</h3>
          <span class="count">{{ stage.candidates.length }}</span>
        </div>
        <div class="candidates-list">
          <div v-for="candidate in stage.candidates" :key="candidate.id" class="candidate-card">
            <div class="candidate-header">
              <div class="avatar">{{ candidate.name.charAt(0) }}</div>
              <div class="candidate-info">
                <h4>{{ candidate.name }}</h4>
                <p>{{ candidate.position }}</p>
              </div>
            </div>
            <div class="candidate-meta">
              <span class="source">{{ candidate.source }}</span>
              <span class="date">{{ candidate.appliedDate }}</span>
            </div>
            <div class="candidate-actions">
              <button class="action-btn" title="View Profile">👁️</button>
              <button class="action-btn" title="Schedule Interview">📅</button>
              <button class="action-btn" title="Move to Next Stage">→</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="candidates-table-section">
      <div class="section-header">
        <h3>All Candidates</h3>
        <div class="filters">
          <select v-model="filterStatus" class="filter-select">
            <option value="">All Status</option>
            <option value="new">New</option>
            <option value="screening">Screening</option>
            <option value="interview">Interview</option>
            <option value="offer">Offer</option>
            <option value="hired">Hired</option>
          </select>
          <input type="text" v-model="searchQuery" placeholder="Search candidates..." class="search-input" />
        </div>
      </div>
      <table class="candidates-table">
        <thead>
          <tr>
            <th>Candidate</th>
            <th>Position</th>
            <th>Stage</th>
            <th>Source</th>
            <th>Applied</th>
            <th>Score</th>
            <th>Actions</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="candidate in filteredCandidates" :key="candidate.id">
            <td>
              <div class="candidate-cell">
                <div class="avatar-small">{{ candidate.name.charAt(0) }}</div>
                <div>
                  <strong>{{ candidate.name }}</strong>
                  <p>{{ candidate.email }}</p>
                </div>
              </div>
            </td>
            <td>{{ candidate.position }}</td>
            <td><span :class="['stage-badge', candidate.stage]">{{ candidate.stage }}</span></td>
            <td>{{ candidate.source }}</td>
            <td>{{ candidate.appliedDate }}</td>
            <td>
              <div class="score-bar">
                <div class="score-fill" :style="{ width: candidate.score + '%' }"></div>
              </div>
              <span class="score-value">{{ candidate.score }}</span>
            </td>
            <td>
              <div class="action-buttons">
                <button class="btn-small">View</button>
                <button class="btn-small btn-outline">Schedule</button>
              </div>
            </td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const filterStatus = ref('')
const searchQuery = ref('')

const stages = ref([
  {
    id: 'applied',
    name: 'Applied',
    candidates: [
      { id: 1, name: 'Tom Wilson', position: 'Frontend Developer', email: 'tom.w@email.com', source: 'LinkedIn', appliedDate: 'May 5, 2026' },
      { id: 2, name: 'Jessica Lee', position: 'Product Manager', email: 'jess.lee@email.com', source: 'Referral', appliedDate: 'May 6, 2026' }
    ]
  },
  {
    id: 'screening',
    name: 'Screening',
    candidates: [
      { id: 3, name: 'Marcus Brown', position: 'Backend Engineer', email: 'marcus.b@email.com', source: 'Indeed', appliedDate: 'May 3, 2026' },
      { id: 4, name: 'Amanda Chen', position: 'UI/UX Designer', email: 'amanda.c@email.com', source: 'LinkedIn', appliedDate: 'May 4, 2026' }
    ]
  },
  {
    id: 'interview',
    name: 'Interview',
    candidates: [
      { id: 5, name: 'Ryan Taylor', position: 'Data Scientist', email: 'ryan.t@email.com', source: 'Company Website', appliedDate: 'Apr 28, 2026' }
    ]
  },
  {
    id: 'offer',
    name: 'Offer',
    candidates: [
      { id: 6, name: 'Sarah Kim', position: 'DevOps Engineer', email: 'sarah.k@email.com', source: 'Referral', appliedDate: 'Apr 20, 2026' }
    ]
  },
  {
    id: 'hired',
    name: 'Hired',
    candidates: [
      { id: 7, name: 'David Martinez', position: 'Full Stack Developer', email: 'david.m@email.com', source: 'LinkedIn', appliedDate: 'Apr 15, 2026' }
    ]
  }
])

const allCandidates = ref([
  { id: 1, name: 'Tom Wilson', position: 'Frontend Developer', email: 'tom.w@email.com', stage: 'new', source: 'LinkedIn', appliedDate: 'May 5, 2026', score: 78 },
  { id: 2, name: 'Jessica Lee', position: 'Product Manager', email: 'jess.lee@email.com', stage: 'new', source: 'Referral', appliedDate: 'May 6, 2026', score: 85 },
  { id: 3, name: 'Marcus Brown', position: 'Backend Engineer', email: 'marcus.b@email.com', stage: 'screening', source: 'Indeed', appliedDate: 'May 3, 2026', score: 72 },
  { id: 4, name: 'Amanda Chen', position: 'UI/UX Designer', email: 'amanda.c@email.com', stage: 'screening', source: 'LinkedIn', appliedDate: 'May 4, 2026', score: 90 },
  { id: 5, name: 'Ryan Taylor', position: 'Data Scientist', email: 'ryan.t@email.com', stage: 'interview', source: 'Company Website', appliedDate: 'Apr 28, 2026', score: 88 },
  { id: 6, name: 'Sarah Kim', position: 'DevOps Engineer', email: 'sarah.k@email.com', stage: 'offer', source: 'Referral', appliedDate: 'Apr 20, 2026', score: 92 },
  { id: 7, name: 'David Martinez', position: 'Full Stack Developer', email: 'david.m@email.com', stage: 'hired', source: 'LinkedIn', appliedDate: 'Apr 15, 2026', score: 95 }
])

const filteredCandidates = computed(() => {
  return allCandidates.value.filter(candidate => {
    const matchesStatus = !filterStatus.value || candidate.stage === filterStatus.value
    const matchesSearch = !searchQuery.value || 
      candidate.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      candidate.position.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchesStatus && matchesSearch
  })
})
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h2 {
  color: #333;
}

.btn-primary {
  background: #4CAF50;
  color: white;
  border: none;
  padding: 12px 24px;
  border-radius: 8px;
  font-size: 14px;
  font-weight: 500;
  cursor: pointer;
  transition: background 0.3s;
}

.btn-primary:hover {
  background: #43a047;
}

.pipeline-overview {
  display: flex;
  gap: 16px;
  margin-bottom: 30px;
  overflow-x: auto;
  padding-bottom: 10px;
}

.pipeline-column {
  flex: 1;
  min-width: 200px;
  background: #f9f9f9;
  border-radius: 12px;
  padding: 16px;
}

.column-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.column-header h3 {
  font-size: 14px;
  color: #333;
}

.count {
  background: #e0e0e0;
  padding: 2px 8px;
  border-radius: 12px;
  font-size: 12px;
  color: #666;
}

.candidates-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.candidate-card {
  background: white;
  padding: 16px;
  border-radius: 8px;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.candidate-header {
  display: flex;
  gap: 12px;
  margin-bottom: 12px;
}

.avatar {
  width: 40px;
  height: 40px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
}

.candidate-info h4 {
  font-size: 14px;
  color: #333;
}

.candidate-info p {
  font-size: 12px;
  color: #888;
}

.candidate-meta {
  display: flex;
  justify-content: space-between;
  font-size: 11px;
  color: #888;
  margin-bottom: 12px;
}

.candidate-actions {
  display: flex;
  gap: 8px;
}

.action-btn {
  background: none;
  border: 1px solid #e0e0e0;
  padding: 6px 10px;
  border-radius: 6px;
  cursor: pointer;
  font-size: 14px;
  transition: all 0.2s;
}

.action-btn:hover {
  background: #f5f5f5;
}

.candidates-table-section {
  background: white;
  border-radius: 12px;
  padding: 24px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.section-header h3 {
  color: #333;
}

.filters {
  display: flex;
  gap: 12px;
}

.filter-select {
  padding: 10px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
}

.search-input {
  padding: 10px 16px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
  width: 200px;
}

.candidates-table {
  width: 100%;
  border-collapse: collapse;
}

.candidates-table th {
  text-align: left;
  padding: 12px 16px;
  border-bottom: 2px solid #f0f0f0;
  color: #888;
  font-weight: 500;
  font-size: 13px;
}

.candidates-table td {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
  font-size: 14px;
}

.candidate-cell {
  display: flex;
  gap: 12px;
  align-items: center;
}

.avatar-small {
  width: 36px;
  height: 36px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 14px;
}

.candidate-cell p {
  color: #888;
  font-size: 12px;
}

.stage-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.stage-badge.new { background: #e3f2fd; color: #2196F3; }
.stage-badge.screening { background: #fff3e0; color: #FF9800; }
.stage-badge.interview { background: #f3e5f5; color: #9C27B0; }
.stage-badge.offer { background: #e8f5e9; color: #4CAF50; }
.stage-badge.hired { background: #4CAF50; color: white; }

.score-bar {
  width: 60px;
  height: 6px;
  background: #f0f0f0;
  border-radius: 3px;
  display: inline-block;
  vertical-align: middle;
  margin-right: 8px;
}

.score-fill {
  height: 100%;
  background: linear-gradient(90deg, #667eea 0%, #764ba2 100%);
  border-radius: 3px;
}

.score-value {
  font-size: 12px;
  color: #666;
}

.action-buttons {
  display: flex;
  gap: 8px;
}

.btn-small {
  padding: 6px 12px;
  border: none;
  border-radius: 6px;
  font-size: 12px;
  cursor: pointer;
  background: #4CAF50;
  color: white;
}

.btn-outline {
  background: white;
  border: 1px solid #4CAF50;
  color: #4CAF50;
}
</style>