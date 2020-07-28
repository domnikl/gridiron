import axios from 'axios';

const client = axios.create({
  baseURL: process.env.VUE_APP_API_BASE_URL,
});

client.login = () => {
};

client.interceptors.response.use(
  (response) => response,
  (error) => {
    if (axios.isCancel(error)) {
      return Promise.reject(error);
    }

    const { response: { status } } = error;

    if (status === 401) {
      client.login();
    }

    return Promise.reject(error);
  },
);

export default client;
