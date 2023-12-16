import * as z from 'zod';

const config = {
  apiKey: import.meta.env.VITE_FIREBASE_API_KEY,
  authDomain: import.meta.env.VITE_FIREBASE_AUTH_DOMAIN,
  projectId: import.meta.env.VITE_FIREBASE_PROJECT_ID,
  appId: import.meta.env.VITE_FIREBASE_APP_ID,
};

const firebaseConfigSchema = z.object({
  apiKey: z.string().min(1),
  authDomain: z.string().min(1),
  projectId: z.string().min(1),
  appId: z.string().min(1),
});

export function getFirebaseConfig() {
  try {
    return firebaseConfigSchema.parse(config);
  } catch (err) {
    console.log('Invalid Firebase config provided');
    if (err instanceof z.ZodError) {
      console.log(err.issues);
    }
    throw err;
  }
}
