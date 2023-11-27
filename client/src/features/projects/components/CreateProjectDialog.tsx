import { useDispatch } from "react-redux";
import { useCreateProjectMutation } from "../store/projectApiSlice";
import * as z from "zod";
import {
  Form,
  FormControl,
  FormField,
  FormItem,
  FormLabel,
  FormMessage,
} from "@/shadcnui/ui/form";
import {
  Dialog,
  DialogContent,
  DialogDescription,
  DialogHeader,
  DialogTitle,
  DialogTrigger,
} from "@/shadcnui/ui/dialog";
import { Input } from "@/shadcnui/ui/input";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { Button } from "@/shadcnui/ui/button";
import { Textarea } from "@/shadcnui/ui/textarea";
import { toast } from "react-toastify";
import { useState } from "react";
import { isApiError } from "@/store/apiError";

const schema = z.object({
  name: z.string().min(3).max(64),
  code: z.string().min(3).max(8),
  description: z.string().min(3).max(256),
});

function CreateProjectForm({ onSuccess }: { onSuccess: () => void }) {
  const dispatch = useDispatch();
  const [createProject, { isLoading }] = useCreateProjectMutation();
  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      name: "",
      code: "",
      description: "",
    },
  });

  async function onSubmit() {
    try {
      await createProject({ ...form.getValues() }).unwrap();
      toast.success("Project created successfully");
      onSuccess();
      form.reset();
    } catch (err) {
      if (isApiError(err)) {
        console.log(err.data.timestamp);
        toast.error(err.data.errors.msg);
      } else {
        toast.error(
          "An error occured while creating a new project. Please try again later."
        );
      }
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="name"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Name</FormLabel>
              <FormControl>
                <Input placeholder="Your project name" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="code"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Code</FormLabel>
              <FormControl>
                <Input placeholder="A short code for your project" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="description"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Describe</FormLabel>
              <FormControl>
                <Textarea placeholder="Describe your project" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />
        <Button
          className="bg-blue-600 hover:bg-blue-800 w-full"
          disabled={isLoading}
        >
          {isLoading ? "Please wait..." : "Create"}
        </Button>
      </form>
    </Form>
  );
}

export function CreateProjectDialog({ disabled }: { disabled: boolean }) {
  const [open, setOpen] = useState(false);

  return (
    <Dialog open={open} onOpenChange={setOpen}>
      <DialogTrigger asChild>
        <Button className="bg-blue-600 hover:bg-blue-800" disabled={disabled}>
          New Project
        </Button>
      </DialogTrigger>
      <DialogContent>
        <DialogHeader>
          <DialogTitle>New Project</DialogTitle>
          <DialogDescription className="text-gray-600">
            A project is where your kanri issues reside. A project can represent
            a business unit, an offering or any other criteria for grouping your
            issues.
          </DialogDescription>
        </DialogHeader>
        <div>{<CreateProjectForm onSuccess={() => setOpen(false)} />}</div>
      </DialogContent>
    </Dialog>
  );
}
