<template>
  <div class="jobs-page">
    <div class="page-header">
      <h2>Job Postings</h2>
      <button class="btn-primary">+ Create Job</button>
    </div>

    <div class="jobs-stats">
      <div class="stat-card">
        <span class="stat-number">{{ stats.active }}</span>
        <span class="stat-label">Active Jobs</span>
      </div>
      <div class="stat-card">
        <span class="stat-number">{{ stats.totalApplications }}</span>
        <span class="stat-label">Total Applications</span>
      </div>
      <div class="stat-card">
        <span class="stat-number">{{ stats.openRoles }}</span>
        <span class="stat-label">Open Roles</span>
      </div>
      <div class="stat-card">
        <span class="stat-number">{{ stats.avgTimeToHire }}</span>
        <span class="stat-label">Avg Days to Hire</span>
      </div>
    </div>

    <div class="jobs-grid">
      <div v-for="job in jobs" :key="job.id" class="job-card">
        <div class="job-header">
          <div class="job-icon">{{ job.department.charAt(0) }}</div>
          <div class="job-title-section">
            <h3>{{ job.title }}</h3>
            <p>{{ job.department }} • {{ job.location }}</p>
          </div>
          <span :class="['status-badge', job.status]">{{ job.status }}</span>
        </div>
        <div class="job-details">
          <div class="detail-item">
            <span class="label">Salary Range</span>
            <span class="value">${{ job.salaryMin.toLocaleString() }} - ${{ job.salaryMax.toLocaleString() }}</span>
          </div>
          <div class="detail-item">
            <span class="label">Experience</span>
            <span class="value">{{ job.experience }}</span>
          </div>
          <div class="detail-item">
            <span class="label">Applications</span>
            <span class="value">{{ job.applications }}</span>
          </div>
          <div class="detail-item">
            <span class="label">Posted</span>
            <span class="value">{{ job.postedDate }}</span>
          </div>
        </div>
        <div class="job-skills">
          <span v-for="skill in job.skills" :key="skill" class="skill-tag">{{ skill }}</span>
        </div>
        <div class="job-actions">
          <button class="btn-small">View Applicants</button>
          <button class="btn-small btn-outline">Edit</button>
          <button class="btn-small btn-danger">Close</button>
        </div>
      </div>
    </div>

    <div class="create-job-modal" v-if="showModal">
      <div class="modal-content">
        <h3>Create New Job</h3>
        <form @submit.prevent="createJob">
          <div class="form-group">
            <label>Job Title</label>
            <input type="text" v-model="newJob.title" required />
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Department</label>
              <select v-model="newJob.department">
                <option>Engineering</option>
                <option>Product</option>
                <option>Design</option>
                <option>Marketing</option>
                <option>Sales</option>
              </select>
            </div>
            <div class="form-group">
              <label>Location</label>
              <input type="text" v-model="newJob.location" required />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>Salary Min</label>
              <input type="number" v-model="newJob.salaryMin" required />
            </div>
            <div class="form-group">
              <label>Salary Max</label>
              <input type="number" v-model="newJob.salaryMax" required />
            </div>
          </div>
          <div class="form-group">
            <label>Description</label>
            <textarea v-model="newJob.description" rows="4"></textarea>
          </div>
          <div class="modal-actions">
            <button type="button" class="btn-secondary" @click="showModal = false">Cancel</button>
            <button type="submit" class="btn-primary">Create Job</button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const showModal = ref(false)

const newJob = ref({
  title: '',
  department: 'Engineering',
  location: '',
  salaryMin: 0,
  salaryMax: 0,
  description: ''
})

const stats = ref({
  active: 12,
  totalApplications: 847,
  openRoles: 18,
  avgTimeToHire: 24
})

const jobs = ref([
  {
    id: 1,
    title: 'Senior Frontend Developer',
    department: 'Engineering',
    location: 'San Francisco, CA',
    salaryMin: 150000,
    salaryMax: 200000,
    experience: '5+ years',
    applications: 45,
    postedDate: 'May 1, 2026',
    status: 'active',
    skills: ['Vue.js', 'React', 'TypeScript', 'CSS']
  },
  {
    id: 2,
    title: 'Product Manager',
    department: 'Product',
    location: 'New York, NY',
    salaryMin: 130000,
    salaryMax: 180000,
    experience: '4+ years',
    applications: 38,
    postedDate: 'Apr 28, 2026',
    status: 'active',
    skills: ['Agile', 'Product Strategy', 'Analytics', 'SQL']
  },
  {
    id: 3,
    title: 'UI/UX Designer',
    department: 'Design',
    location: 'Remote',
    salaryMin: 100000,
    salaryMax: 140000,
    experience: '3+ years',
    applications: 52,
    postedDate: 'Apr 25, 2026',
    status: 'active',
    skills: ['Figma', 'User Research', 'Prototyping', 'Design Systems']
  },
  {
    id: 4,
    title: 'Backend Engineer',
    department: 'Engineering',
    location: 'Austin, TX',
    salaryMin: 140000,
    salaryMax: 190000,
    experience: '4+ years',
    applications: 29,
    postedDate: 'Apr 20, 2026',
    status: 'paused',
    skills: ['Node.js', 'Python', 'PostgreSQL', 'AWS']
  }
])

const createJob = () => {
  showModal.value = false
}
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
}

.jobs-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 30px;
}

.stat-card {
  background: white;
  padding: 20px;
  border-radius: 12px;
  text-align: center;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.stat-number {
  display: block;
  font-size: 32px;
  font-weight: bold;
  color: #333;
}

.stat-label {
  font-size: 13px;
  color: #888;
}

.jobs-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 20px;
}

.job-card {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.job-header {
  display: flex;
  align-items: flex-start;
  gap: 16px;
  margin-bottom: 20px;
}

.job-icon {
  width: 50px;
  height: 50px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 20px;
  font-weight: bold;
}

.job-title-section {
  flex: 1;
}

.job-title-section h3 {
  font-size: 18px;
  color: #333;
  margin-bottom: 4px;
}

.job-title-section p {
  font-size: 13px;
  color: #888;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 20px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background: #e8f5e9;
  color: #4CAF50;
}

.status-badge.paused {
  background: #fff3e0;
  color: #FF9800;
}

.status-badge.closed {
  background: #ffebee;
  color: #f44336;
}

.job-details {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
  margin-bottom: 20px;
}

.detail-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.detail-item .label {
  font-size: 12px;
  color: #888;
}

.detail-item .value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.job-skills {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
  margin-bottom: 20px;
}

.skill-tag {
  background: #f0f4ff;
  color: #667eea;
  padding: 4px 10px;
  border-radius: 6px;
  font-size: 12px;
}

.job-actions {
  display: flex;
  gap: 10px;
}

.btn-small {
  padding: 8px 16px;
  border: none;
  border-radius: 6px;
  font-size: 13px;
  cursor: pointer;
  background: #4CAF50;
  color: white;
}

.btn-outline {
  background: white;
  border: 1px solid #e0e0e0;
  color: #666;
}

.btn-danger {
  background: #f44336;
}

.create-job-modal {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0,0,0,0.5);
  display: flex;
  align-items: center;
  justify-content: center;
  z-index: 1000;
}

.modal-content {
  background: white;
  padding: 32px;
  border-radius: 16px;
  width: 500px;
  max-width: 90%;
}

.modal-content h3 {
  margin-bottom: 24px;
  color: #333;
}

.form-group {
  margin-bottom: 16px;
}

.form-group label {
  display: block;
  margin-bottom: 6px;
  font-size: 13px;
  color: #666;
}

.form-group input,
.form-group select,
.form-group textarea {
  width: 100%;
  padding: 10px 14px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
}

.form-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.modal-actions {
  display: flex;
  justify-content: flex-end;
  gap: 12px;
  margin-top: 24px;
}

.btn-secondary {
  padding: 10px 20px;
  border: 1px solid #e0e0e0;
  background: white;
  border-radius: 8px;
  cursor: pointer;
}
</style>