import React from 'react';
import ReactDOM from 'react-dom';
import './index.css';
import App from './App';
import * as serviceWorker from './serviceWorker';
import { BrowserRouter } from 'react-router-dom';
import { HelmetProvider } from 'react-helmet-async'; //브라우저 타이틀
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';


axios.defaults.headers.common['X-AUTH-TOKEN'] = sessionStorage.getItem('user');
ReactDOM.render(
  <BrowserRouter>
    <HelmetProvider>
        <App />
    </HelmetProvider>
  </BrowserRouter>,
  document.getElementById('root'),
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
