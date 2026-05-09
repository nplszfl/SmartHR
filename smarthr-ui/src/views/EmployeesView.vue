<template>
  <div class="employees-page">
    <div class="page-header">
      <h2>Employee Dashboard</h2>
      <button class="btn-primary">+ Add Employee</button>
    </div>

    <div class="employees-stats">
      <div class="stat-card">
        <span class="stat-number">{{ stats.total }}</span>
        <span class="stat-label">Total Employees</span>
      </div>
      <div class="stat-card">
        <span class="stat-number">{{ stats.departments }}</span>
        <span class="stat-label">Departments</span>
      </div>
      <div class="stat-card">
        <span class="stat-number">{{ stats.newHires }}</span>
        <span class="stat-label">New This Month</span>
      </div>
      <div class="stat-card">
        <span class="stat-number">{{ stats.openPositions }}</span>
        <span class="stat-label">Open Positions</span>
      </div>
    </div>

    <div class="employees-layout">
      <div class="department-breakdown">
        <h3>Department Breakdown</h3>
        <div class="dept-chart">
          <div v-for="dept in departments" :key="dept.name" class="dept-bar-container">
            <div class="dept-info">
              <span class="dept-name">{{ dept.name }}</span>
              <span class="dept-count">{{ dept.count }}</span>
            </div>
            <div class="dept-bar">
              <div class="dept-fill" :style="{ width: (dept.count / stats.total * 100) + '%', background: dept.color }"></div>
            </div>
          </div>
        </div>
      </div>

      <div class="recent-hires">
        <h3>Recent Hires</h3>
        <div class="hire-list">
          <div v-for="hire in recentHires" :key="hire.id" class="hire-card">
            <div class="avatar">{{ hire.name.charAt(0) }}</div>
            <div class="hire-info">
              <h4>{{ hire.name }}</h4>
              <p>{{ hire.position }}</p>
              <span class="start-date">Started {{ hire.startDate }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="employees-table-section">
      <div class="section-header">
        <h3>All Employees</h3>
        <div class="filters">
          <select v-model="filterDept" class="filter-select">
            <option value="">All Departments</option>
            <option value="Engineering">Engineering</option>
            <option value="Product">Product</option>
            <option value="Design">Design</option>
            <option value="Marketing">Marketing</option>
          </select>
          <input type="text" v-model="searchQuery" placeholder="Search employees..." class="search-input" />
        </div>
      </div>
      <table class="employees-table">
        <thead>
          <tr>
            <th>Employee</th>
            <th>Department</th>
            <th>Position</th>
            <th>Email</th>
            <th>Start Date</th>
            <th>Status</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="employee in filteredEmployees" :key="employee.id">
            <td>
              <div class="employee-cell">
                <div class="avatar-small">{{ employee.name.charAt(0) }}</div>
                <div>
                  <strong>{{ employee.name }}</strong>
                </div>
              </div>
            </td>
            <td>{{ employee.department }}</td>
            <td>{{ employee.position }}</td>
            <td>{{ employee.email }}</td>
            <td>{{ employee.startDate }}</td>
            <td><span :class="['status-badge', employee.status]">{{ employee.status }}</span></td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script setup>
import { ref, computed } from 'vue'

const filterDept = ref('')
const searchQuery = ref('')

const stats = ref({
  total: 458,
  departments: 8,
  newHires: 12,
  openPositions: 18
})

const departments = ref([
  { name: 'Engineering', count: 156, color: '#667eea' },
  { name: 'Product', count: 68, color: '#764ba2' },
  { name: 'Design', count: 45, color: '#f093fb' },
  { name: 'Marketing', count: 52, color: '#4CAF50' },
  { name: 'Sales', count: 78, color: '#FF9800' },
  { name: 'Operations', count: 38, color: '#2196F3' },
  { name: 'HR', count: 15, color: '#e91e63' },
  { name: 'Finance', count: 26, color: '#00bcd4' }
])

const recentHires = ref([
  { id: 1, name: 'David Kim', position: 'Product Manager', startDate: 'May 5, 2026' },
  { id: 2, name: 'Lisa Wang', position: 'Senior Developer', startDate: 'May 3, 2026' },
  { id: 3, name: 'Michael Chen', position: 'UX Designer', startDate: 'May 1, 2026' },
  { id: 4, name: 'Sarah Johnson', position: 'Marketing Lead', startDate: 'Apr 28, 2026' }
])

const employees = ref([
  { id: 1, name: 'Alice Cooper', department: 'Engineering', position: 'Tech Lead', email: 'alice.c@company.com', startDate: 'Jan 15, 2022', status: 'active' },
  { id: 2, name: 'Bob Smith', department: 'Engineering', position: 'Senior Developer', email: 'bob.s@company.com', startDate: 'Mar 20, 2023', status: 'active' },
  { id: 3, name: 'Carol White', department: 'Product', position: 'Product Manager', email: 'carol.w@company.com', startDate: 'Jun 10, 2022', status: 'active' },
  { id: 4, name: 'Dan Brown', department: 'Design', position: 'UI Designer', email: 'dan.b@company.com', startDate: 'Sep 5, 2023', status: 'active' },
  { id: 5, name: 'Eve Davis', department: 'Marketing', position: 'Marketing Manager', email: 'eve.d@company.com', startDate: 'Feb 1, 2024', status: 'active' },
  { id: 6, name: 'Frank Miller', department: 'Sales', position: 'Sales Director', email: 'frank.m@company.com', startDate: 'Apr 15, 2021', status: 'active' },
  { id: 7, name: 'Grace Lee', department: 'HR', position: 'HR Manager', email: 'grace.l@company.com', startDate: 'Jul 20, 2022', status: 'active' },
  { id: 8, name: 'Henry Wilson', department: 'Finance', position: 'Financial Analyst', email: 'henry.w@company.com', startDate: 'Nov 8, 2023', status: 'on-leave' }
])

const filteredEmployees = computed(() => {
  return employees.value.filter(emp => {
    const matchesDept = !filterDept.value || emp.department === filterDept.value
    const matchesSearch = !searchQuery.value || 
      emp.name.toLowerCase().includes(searchQuery.value.toLowerCase()) ||
      emp.position.toLowerCase().includes(searchQuery.value.toLowerCase())
    return matchesDept && matchesSearch
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
  cursor: pointer;
}

.employees-stats {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
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

.employees-layout {
  display: grid;
  grid-template-columns: 1.2fr 1fr;
  gap: 24px;
  margin-bottom: 24px;
}

.department-breakdown,
.recent-hires {
  background: white;
  padding: 24px;
  border-radius: 12px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.department-breakdown h3,
.recent-hires h3 {
  margin-bottom: 20px;
  color: #333;
}

.dept-chart {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.dept-bar-container {
  display: flex;
  flex-direction: column;
  gap: 6px;
}

.dept-info {
  display: flex;
  justify-content: space-between;
  font-size: 13px;
}

.dept-name {
  color: #333;
}

.dept-count {
  color: #888;
  font-weight: 500;
}

.dept-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.dept-fill {
  height: 100%;
  border-radius: 4px;
  transition: width 0.5s ease;
}

.hire-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.hire-card {
  display: flex;
  gap: 16px;
  align-items: center;
  padding: 12px;
  background: #f9f9f9;
  border-radius: 10px;
}

.avatar {
  width: 48px;
  height: 48px;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-weight: bold;
  font-size: 18px;
}

.hire-info h4 {
  font-size: 14px;
  color: #333;
  margin-bottom: 2px;
}

.hire-info p {
  font-size: 13px;
  color: #666;
}

.start-date {
  font-size: 11px;
  color: #888;
}

.employees-table-section {
  background: white;
  padding: 24px;
  border-radius: 12px;
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

.employees-table {
  width: 100%;
  border-collapse: collapse;
}

.employees-table th {
  text-align: left;
  padding: 12px 16px;
  border-bottom: 2px solid #f0f0f0;
  color: #888;
  font-weight: 500;
  font-size: 13px;
}

.employees-table td {
  padding: 16px;
  border-bottom: 1px solid #f0f0f0;
  color: #333;
  font-size: 14px;
}

.employee-cell {
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

.status-badge {
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 12px;
}

.status-badge.active {
  background: #e8f5e9;
  color: #4CAF50;
}

.status-badge.on-leave {
  background: #fff3e0;
  color: #FF9800;
}
</style>