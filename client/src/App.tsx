import '@mantine/core/styles.css';
import { MantineProvider } from '@mantine/core';
import { Notifications } from '@mantine/notifications';
import { Router } from './Router';
import { theme } from './theme';
import { Provider } from 'react-redux';
import { store } from './store';

import '@mantine/notifications/styles.css';

export default function App() {
  return (
    <Provider store={store}>
      <MantineProvider theme={theme}>
        <Notifications />
        <Router />
      </MantineProvider>
    </Provider>
  );
}
