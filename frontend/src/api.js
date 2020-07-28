import axios from 'axios';

const client = axios.create({
  baseURL: 'http://192.168.178.52:4001', // TODO: remove hard-coded value
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
