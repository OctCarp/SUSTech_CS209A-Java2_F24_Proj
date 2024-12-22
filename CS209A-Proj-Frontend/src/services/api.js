import axios from 'axios';

export const serverUrl = 'http://localhost:8081/api';
// export const serverUrl = 'http://10.26.2.214:8081';

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
export const fetchTopBugData = async (numBugs, bugType, startDate, endDate) => {
    const url = `${serverUrl}/bugs/top/frequency?limit=${numBugs}&type=${bugType}`;
    // const url = `${serverUrl}/bugs/top-frequency?limit=${numBugs}&type=${bugType}&start_date=${startDate}&end_date=${endDate}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取话题详细数据
export const fetchTopicStatisticsData = async (topicName, startDate, endDate) => {
    const url = `${serverUrl}/topics/name/${topicName}/statistics`;
    // const url = `${serverUrl}/topics/name/{topic-name}/statistics?topic-name=${topicName}&start_date=${startDate}&end_date=${endDate}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取故障详细数据
export const fetchBugStatisticsData = async (bugName, startDate, endDate) => {
    const url = `${serverUrl}/bugs/name/${bugName}/statistics`;
    // const url = `${serverUrl}/bugs/name/{bug-name}/statistics?bug-name=${bugName}&start_date=${startDate}&end_date=${endDate}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

export const fetchTopTopicEngagementData = async (numTopics, minReputation) => {
    const url = `${serverUrl}/topics/top/engagement?limit=${numTopics}&min_reputation=${minReputation}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

export const fetchAnswerQualityData = async () => {
    const url = `${serverUrl}/answers/all/quality/static`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

export const fetchAllTopicName = async () => {
    const url = `${serverUrl}/topics/all/brief`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

export const fetchAllBugName = async () => {
    const url = `${serverUrl}/bugs/top/frequency/brief`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};
