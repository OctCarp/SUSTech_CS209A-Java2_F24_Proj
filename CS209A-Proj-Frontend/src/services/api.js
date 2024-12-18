import axios from 'axios';

export const serverUrl = 'http://localhost:8081';

// 创建一个 Axios 实例
const apiClient = axios.create({
    baseURL: serverUrl,
    withCredentials: true,
});

// 获取高频话题数据
export const fetchTopTopicData = async (numTopics, startDate, endDate) => {
    const url = `${serverUrl}/topics/top/frequency?limit=${numTopics}`;
    // const url = `${serverUrl}/topics/top/frequency?limit=${numTopics}&start_date=${startDate}&end_date=${endDate}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取高频故障数据
export const fetchTopBugData = async (numBugs, startDate, endDate) => {
    const url = `${serverUrl}/bugs/top-frequency?limit=${numBugs}`;
    // const url = `${serverUrl}/bugs/top-frequency?limit=${numBugs}&start_date=${startDate}&end_date=${endDate}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取话题详细数据
export const fetchTopicStatisticsData = async (topicName) => {
    const url = `${serverUrl}/topics/statistics?topic_name=${topicName}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取故障详细数据
export const fetchBugStatisticsData = async (bugName) => {
    const url = `${serverUrl}/bugs/statistics?bug_name=${bugName}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

export const fetchTopTopicEngagementData = async (numTopics, minReputation) => {
    const url = `${serverUrl}/topics/top/engagement?limit=${numTopics}$min_reputation=${minReputation}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};
