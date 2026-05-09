<template>
  <div id="app">
    <div class="app-layout">
      <aside class="sidebar">
        <div class="logo">
          <h1>SmartHR</h1>
        </div>
        <nav class="nav-menu">
          <router-link to="/" class="nav-item">
            <span class="icon">📊</span>
            Dashboard
          </router-link>
          <router-link to="/candidates" class="nav-item">
            <span class="icon">👥</span>
            Candidates
          </router-link>
          <router-link to="/jobs" class="nav-item">
            <span class="icon">💼</span>
            Jobs
          </router-link>
          <router-link to="/interviews" class="nav-item">
            <span class="icon">📅</span>
            Interviews
          </router-link>
          <router-link to="/employees" class="nav-item">
            <span class="icon">👔</span>
            Employees
          </router-link>
          <router-link to="/insights" class="nav-item">
            <span class="icon">🤖</span>
            AI Insights
          </router-link>
        </nav>
      </aside>
      <main class="main-content">
        <header class="top-bar">
          <input type="text" placeholder="Search..." class="search-input" />
          <div class="user-info">
            <span>Admin User</span>
            <div class="avatar">A</div>
          </div>
        </header>
        <div class="page-content">
          <router-view />
        </div>
      </main>
    </div>
  </div>
</template>

<script setup>
</script>

<style>
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
  background: #f5f6fa;
}

#app {
  height: 100vh;
  overflow: hidden;
}

.app-layout {
  display: flex;
  height: 100%;
}

.sidebar {
  width: 240px;
  background: #1a1a2e;
  color: white;
  padding: 20px 0;
  display: flex;
  flex-direction: column;
}

.logo {
  padding: 0 20px 30px;
  border-bottom: 1px solid rgba(255,255,255,0.1);
}

.logo h1 {
  font-size: 24px;
  color: #4CAF50;
}

.nav-menu {
  flex: 1;
  padding-top: 20px;
}

.nav-item {
  display: flex;
  align-items: center;
  padding: 15px 20px;
  color: rgba(255,255,255,0.7);
  text-decoration: none;
  transition: all 0.3s;
  gap: 12px;
}

.nav-item:hover,
.nav-item.router-link-active {
  background: rgba(255,255,255,0.1);
  color: white;
}

.nav-item .icon {
  font-size: 20px;
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.top-bar {
  height: 70px;
  background: white;
  padding: 0 30px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  box-shadow: 0 2px 10px rgba(0,0,0,0.05);
}

.search-input {
  width: 300px;
  padding: 10px 15px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  font-size: 14px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 12px;
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

.page-content {
  flex: 1;
  padding: 30px;
  overflow-y: auto;
}
</style>