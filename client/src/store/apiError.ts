export type apiError = {
  status: number;
  data: {
    errors: { [k: string]: string };
    timestamp: Date;
  };
};

export function isApiError(error: unknown): error is apiError {
  return (
    typeof error === 'object' &&
    error != null &&
    'status' in error &&
    typeof (error as any).status === 'number'
  );
}
