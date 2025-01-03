import axios from 'axios';

export const serverUrl = 'http://localhost:8081/api';
// export const serverUrl = 'http://10.26.2.214:8081';

const apiClient = axios.create({
    baseURL: serverUrl,
    withCredentials: true,
});

// 获取高频话题数据
export const fetchTopTopicData = async (numTopics, startSecond, endSecond) => {
    // const url = `${serverUrl}/topics/top/frequency?limit=${numTopics}`;
    const url = `${serverUrl}/topics/top/frequency/time?limit=${numTopics}&startSecond=${startSecond}&endSecond=${endSecond}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取高频错误/异常数据
export const fetchTopBugData = async (numBugs, bugType) => {
    const url = `${serverUrl}/bugs/top/frequency?limit=${numBugs}&type=${bugType}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取话题详细数据
export const fetchTopicStatisticsData = async (topicName, startSecond, endSecond) => {
    const url = `${serverUrl}/topics/name/${topicName}/statistics?startSecond=${startSecond}&endSecond=${endSecond}`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取错误/异常详细数据
export const fetchBugStatisticsData = async (bugName) => {
    const url = `${serverUrl}/bugs/name/${bugName}/statistics`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};

// 获取高频用户参与话题数据
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

// 获取答案质量数据
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

// 获取所有话题名称
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

// 获取所有错误/异常名称
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

// 获取所有用户声望数据
export const fetchAllUserReputation = async () => {
    const url = `${serverUrl}/users/all/rep-dist`;
    try {
        const response = await apiClient.get(url);
        return response.data;
    } catch (err) {
        console.error("API 请求失败:", err);
        throw err;
    }
};