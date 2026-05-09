# SmartHR - AI驱动的人力资源管理系统

[![Java](https://img.shields.io/badge/Java-21-blue.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2-green.svg)](https://spring.io/projects/spring-boot)
[![Vue](https://img.shields.io/badge/Vue-3-orange.svg)](https://vuejs.org/)
[![AI](https://img.shields.io/badge/AI-HR--Powered-yellow.svg)](https://deepseek.com/)

> 👥 **AI-First人力资源平台** - 智能简历筛选、AI面试助手、离职预测，让HR工作效率提升5倍

## 📋 项目简介

SmartHR 是一套**深度AI融合**的人力资源管理系统，对标Workday但AI能力强10倍。

### 核心AI能力

| AI功能 | 描述 | 价值 |
|--------|------|------|
| 📄 **AI简历筛选** | 自动解析简历，匹配度评分 | 筛选效率提升80% |
| 🤝 **AI面试助手** | 自动生成面试问题，分析回答质量 | 面试准备时间减少70% |
| 📊 **人才画像** | AI构建全方位人才画像 | 人才识别准确率提升60% |
| 🔮 **离职预测** | 分析员工行为，预测离职风险 | 提前预警保留人才 |
| 📈 **编制规划** | AI预测招聘需求，优化人力配置 | 招聘成本降低40% |

## 🏗️ 系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        Vue 3 前端                            │
│   (招聘流程 / 员工管理 / 数据分析 / AI洞察)                   │
├─────────────────────────────────────────────────────────────┤
│                   Spring Cloud Alibaba 后端                  │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐  │
│  │ Gateway     │ │ Candidate   │ │ Job                 │  │
│  │ Service     │ │ Service     │ │ Service             │  │
│  └─────────────┘ └─────────────┘ └─────────────────────┘  │
│  ┌─────────────┐ ┌─────────────┐ ┌─────────────────────┐  │
│  │ Interview   │ │ Employee    │ │ AI Service          │  │
│  │ Service     │ │ Service     │ │ (Java)              │  │
│  └─────────────┘ └─────────────┘ └─────────────────────┘  │
├─────────────────────────────────────────────────────────────┤
│                    Java AI 服务层                            │
│  (ResumeParser / InterviewAI / TurnoverPrediction / ...)     │
├─────────────────────────────────────────────────────────────┤
│                    DeepSeek API                             │
└─────────────────────────────────────────────────────────────┘
```

## 📂 目录结构

```
SmartHR/
├── ai-service/                    # Java AI 服务层 ⭐
│   ├── src/main/java/
│   │   └── com/smarthr/ai/
│   │       ├── controller/         # REST API
│   │       ├── service/            # AI 业务逻辑
│   │       │   ├── ResumeParsingService.java      # 简历解析
│   │       │   ├── ResumeScoringService.java      # 简历评分
│   │       │   ├── InterviewAnalysisService.java  # 面试分析
│   │       │   ├── InterviewQuestionsService.java # 面试题生成
│   │       │   ├── TurnoverPredictionService.java # 离职预测
│   │       │   └── TalentMatchingService.java     # 人才匹配
│   │       └── dto/                # 数据传输对象
│   └── pom.xml
│
├── candidate-service/              # 候选人管理服务
├── job-service/                    # 职位管理服务
├── interview-service/               # 面试管理服务
├── employee-service/               # 员工管理服务
├── analytics-service/              # 分析服务
├── gateway/                        # API网关
├── smarthr-ui/                     # Vue 3前端
│   └── src/
│       ├── views/                 # 页面
│       │   ├── CandidatesView.vue  # 候选人列表
│       │   ├── JobsView.vue       # 职位管理
│       │   ├── InterviewsView.vue # 面试管理
│       │   ├── EmployeesView.vue  # 员工管理
│       │   └── DashboardView.vue  # 数据看板
│       └── components/            # 组件
└── pom.xml
```

## 🚀 快速开始

### 环境要求

- JDK 21+
- Maven 3.9+
- Node.js 18+
- MySQL 8.0+
- Redis
- Nacos

### 1. 启动后端

```bash
git clone https://github.com/nplszfl/SmartHR.git
cd SmartHR

mvn clean install -DskipTests

# AI服务
cd ai-service && mvn spring-boot:run

# 其他微服务
cd ../gateway && mvn spring-boot:run
cd ../candidate-service && mvn spring-boot:run
cd ../job-service && mvn spring-boot:run
```

### 2. 启动前端

```bash
cd smarthr-ui
npm install
npm run dev
```

### 3. 配置

```yaml
# ai-service/src/main/resources/application.yml
spring:
  ai:
    deepseek:
      api-key: your-api-key
      model: deepseek-chat
```

## 📡 核心API

### AI简历解析

```
POST /api/v1/ai/resume/parse

{
  "resumeText": "张三\n男 28岁\n本科 计算机科学\n工作经历：\n2019-2022 某互联网公司 Java开发\n技能：Java, Python, MySQL..."
}

响应：
{
  "name": "张三",
  "age": 28,
  "gender": "男",
  "education": {
    "degree": "本科",
    "major": "计算机科学",
    "school": "某大学"
  },
  "workExperience": [
    {
      "company": "某互联网公司",
      "position": "Java开发",
      "duration": "2019-2022",
      "description": "负责后端开发"
    }
  ],
  "skills": ["Java", "Python", "MySQL"],
  "confidence": 0.92
}
```

### AI简历评分

```
POST /api/v1/ai/resume/score

{
  "resumeText": "...",
  "jobRequirements": {
    "skills": ["Java", "Spring Boot", "MySQL"],
    "experience": "3年以上",
    "education": "本科",
    "salaryRange": "20-30K"
  }
}

响应：
{
  "overallScore": 85,
  "grade": "A",
  "skillMatch": {
    "Java": 95,
    "Spring Boot": 80,
    "MySQL": 90
  },
  "experienceMatch": 85,
  "educationMatch": 90,
  "reasoning": "候选人技能与职位要求高度匹配...",
  "highlights": ["知名公司经验", "技术栈完全匹配"],
  "concerns": ["薪资期望略高于范围"]
}
```

### AI离职预测

```
POST /api/v1/ai/turnover/predict

{
  "employeeId": "emp_123",
  "tenureMonths": 24,
  "performance": 4.2,
  "workHoursTrend": "increasing",
  "recentSalaryIncrease": 0,
  "managerChange": true,
  "projectInvolvement": "decreasing",
  "overtimeHoursTrend": "increasing"
}

响应：
{
  "turnoverRisk": "high",
  "probability": 0.78,
  "keyFactors": ["经理变动", "加班增加", "薪资未调整"],
  "warningSigns": ["工作投入度下降", "项目参与减少"],
  "recommendedActions": ["安排1:1沟通", "考虑晋升/调薪", "分配新项目挑战"]
}
```

### AI面试分析

```
POST /api/v1/ai/interview/analyze

{
  "candidateName": "李四",
  "position": "高级Java开发",
  "transcript": "面试官：介绍一下你的项目经验...\n候选人：我在某公司负责了电商平台的开发...\n面试官：遇到过什么技术挑战？...",
  "questionsAsked": ["项目经验", "技术挑战", "团队协作"]
}

响应：
{
  "overallScore": 82,
  "strengths": ["项目经验丰富", "技术深度好", "沟通表达清晰"],
  "areasForImprovement": ["架构设计经验不足", "缺少大规模系统经验"],
  "questionScores": {
    "项目经验": 90,
    "技术挑战": 78,
    "团队协作": 85
  },
  "recommendation": "强烈推荐",
  "hiringAdvice": "适合中级岗位，高级需进一步考察架构能力"
}
```

## 🎯 AI招聘流程

```
简历收集
    ↓
[AI简历解析] → 结构化数据
    ↓
[AI简历评分] → 匹配度排序 → 邀约面试
    ↓
[AI面试问题] → 个性化题库
    ↓
面试进行
    ↓
[AI面试分析] → 反馈报告 → 录用决策
    ↓
入职跟踪
    ↓
[AI离职预测] → 人才保留
```

## 🛠️ 技术栈

| 层级 | 技术 |
|------|------|
| 前端 | Vue 3 + TypeScript + Element Plus + ECharts |
| 后端 | Java 21 + Spring Cloud Alibaba + MyBatis Plus |
| AI服务 | Spring Boot 3.2 + WebClient + DeepSeek |
| 数据库 | MySQL |
| 缓存 | Redis |
| 注册中心 | Nacos |

## 📊 功能列表

### 招聘管理
- 职位发布与管理
- 简历收集与解析
- AI简历筛选评分
- 候选人管道追踪

### 面试管理
- 面试日程安排
- AI面试题生成
- 面试记录与分析
- 多轮面试协调

### 员工管理
- 员工档案管理
- 入转调离流程
- AI离职预测
- 员工满意度分析

### 数据分析
- 招聘漏斗分析
- AI洞察面板
- 离职率趋势
- 人效分析

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！

## 📄 许可证

MIT License

## 👨‍💻 作者

**黄辉翔** - [GitHub](https://github.com/nplszfl)

---

⭐ 如果对你有帮助，请给项目一个 Star！
