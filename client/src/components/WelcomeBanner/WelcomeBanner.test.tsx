import { render, screen } from '@test-utils';
import { Landing } from './WelcomeBanner';

describe('Welcome component', () => {
  it('has correct Vite guide link', () => {
    render(<Landing />);
    expect(screen.getByText('this guide')).toHaveAttribute(
      'href',
      'https://mantine.dev/guides/vite/'
    );
  });
});
