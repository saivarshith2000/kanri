import { ActionIcon, Button, Menu, useMantineColorScheme } from '@mantine/core';
import { IconSun } from '@tabler/icons-react';

export function ColorSchemeToggle() {
  const { setColorScheme } = useMantineColorScheme();

  return (
    <Menu shadow="md" width="100">
      <Menu.Target>
        <ActionIcon variant="light" aria-label="theme">
          <IconSun style={{ width: '70%', height: '70%' }} stroke={1.5} />
        </ActionIcon>
      </Menu.Target>
      <Menu.Dropdown>
        <Menu.Item onClick={() => setColorScheme('light')}>Light</Menu.Item>
        <Menu.Item onClick={() => setColorScheme('dark')}>Dark</Menu.Item>
        <Menu.Item onClick={() => setColorScheme('auto')}>Auto</Menu.Item>
      </Menu.Dropdown>
    </Menu>
  );
}
