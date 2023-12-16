import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import * as z from "zod";

import { Button } from "@/shadcnui/ui/button";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shadcnui/ui/form";
import { Input } from "@/shadcnui/ui/input";
import { Link } from "react-router-dom";
import { AuthHeader } from "./components/AuthHeader";

const formSchema = z.object({
  email: z.string().email(),
  password: z.string().nonempty(),
});

function SigninForm() {
  const form = useForm<z.infer<typeof formSchema>>({
    resolver: zodResolver(formSchema),
    defaultValues: {
      email: "",
      password: "",
    },
  });

  function onSubmit(values: z.infer<typeof formSchema>) {
    console.log(values);
  }

  return (
    <Form {...form}>
      <form
        onSubmit={form.handleSubmit(onSubmit)}
        className="space-y-4 w-[350px]"
      >
        <FormField
          control={form.control}
          name="email"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Email</FormLabel>
              <FormControl>
                <Input placeholder="Your e-mail address" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <FormField
          control={form.control}
          name="password"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Password</FormLabel>
              <FormControl>
                <Input placeholder="Your password" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button
          type="submit"
          className="w-full bg-green-600 hover:bg-green-700"
        >
          Sign Up
        </Button>
      </form>
    </Form>
  );
}

export function SignupPage() {
  return (
    <div className="min-h-screen flex flex-col justify-content items-center">
      <AuthHeader />
      <div className="flex flex-col gap-y-4 mt-36">
        <p className="text-4xl">Sign Up</p>
        <SigninForm />
        <Link to="/auth/signin" className="text-sm text-center text-blue-500">
          Already have an account ? Sign In
        </Link>
      </div>
    </div>
  );
}
