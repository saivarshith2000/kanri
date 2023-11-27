import { useState } from "react";
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
  Sheet,
  SheetContent,
  SheetHeader,
  SheetTitle,
  SheetTrigger,
} from "@/shadcnui/ui/sheet";
import {
  Select,
  SelectContent,
  SelectItem,
  SelectTrigger,
  SelectValue,
} from "@/shadcnui/ui/select";

import { Button } from "@/shadcnui/ui/button";
import { isApiError } from "@/store/apiError";
import { useCreateIssueMutation } from "../store/issueApiSlice";
import { useForm } from "react-hook-form";
import { zodResolver } from "@hookform/resolvers/zod";
import { toast } from "react-toastify";
import { Input } from "@/shadcnui/ui/input";
import { Textarea } from "@/shadcnui/ui/textarea";

export const ISSUE_TYPES = [
  "EPIC",
  "DEFECT",
  "STORY",
  "TASK",
  "SPIKE",
] as const;
export const ISSUE_PRIORITIES = ["LOW", "MEDIUM", "HIGH", "BLOCKER"] as const;

const schema = z.object({
  summary: z.string().nonempty(),
  description: z.string().nonempty(),
  story_points: z.coerce.number().min(0.5),
  type: z.enum(ISSUE_TYPES),
  priority: z.enum(ISSUE_PRIORITIES),
  // epicCode: z.string(),
  // assigneeEmail: z.string().email(),
});
// .refine(
//   (data) =>
//     data.type != "EPIC" || (data.type == "EPIC" && data.epicCode != ""),
//   { message: "EPIC is required", path: ["epicCode"] }
// );

function CreateIssueForm({
  onSuccess,
  projectCode,
}: {
  onSuccess: () => void;
  projectCode: string;
}) {
  const [createIssue, { isLoading }] = useCreateIssueMutation();
  const form = useForm<z.infer<typeof schema>>({
    resolver: zodResolver(schema),
    defaultValues: {
      summary: "",
      description: "",
      type: "STORY",
      priority: "LOW",
      story_points: 0.5,
      // epicCode: "",
      // assigneeEmail: "",
    },
  });

  async function onSubmit() {
    try {
      await createIssue({
        ...form.getValues(),
        projectCode,
        assigneeEmail: "user1@dev.com", // TODO
        epicCode: "TESTA-1", // TODO
      }).unwrap();
      const issueCode = "ISSUE-CODE"; // TODO
      toast.success(`Issue ${issueCode} created successfully`);
      onSuccess();
      form.reset();
    } catch (err) {
      if (isApiError(err)) {
        console.log(err.data.timestamp);
        toast.error(err.data.errors.msg);
      } else {
        toast.error(
          "An error occured while creating issue. Please try again later."
        );
      }
    }
  }

  return (
    <Form {...form}>
      <form onSubmit={form.handleSubmit(onSubmit)} className="space-y-4">
        <FormField
          control={form.control}
          name="summary"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Summary</FormLabel>
              <FormControl>
                <Input placeholder="Brief summary of your issue" {...field} />
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
              <FormLabel>Description</FormLabel>
              <FormControl>
                <Textarea placeholder="Describe your issue" {...field} />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="story_points"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Story Points</FormLabel>
              <FormControl>
                <Input {...field} type="number" />
              </FormControl>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="type"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Type</FormLabel>
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select your issue's type" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {ISSUE_TYPES.map((t) => (
                    <SelectItem value={t} key={t}>
                      {t}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <FormField
          control={form.control}
          name="priority"
          render={({ field }) => (
            <FormItem>
              <FormLabel>Priority</FormLabel>
              <Select onValueChange={field.onChange} defaultValue={field.value}>
                <FormControl>
                  <SelectTrigger>
                    <SelectValue placeholder="Select your Issue's priority" />
                  </SelectTrigger>
                </FormControl>
                <SelectContent>
                  {ISSUE_PRIORITIES.map((p) => (
                    <SelectItem value={p} key={p}>
                      {p}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
              <FormMessage />
            </FormItem>
          )}
        />

        <Button
          className="bg-blue-600 hover:bg-blue-800 w-full"
          disabled={isLoading}
          type="submit"
        >
          {isLoading ? "Please wait..." : "Create"}
        </Button>
      </form>
    </Form>
  );
}

export function CreateIssueDialog({ projectCode }: { projectCode: string }) {
  const [open, setOpen] = useState(false);

  return (
    <Sheet open={open} onOpenChange={setOpen}>
      <SheetTrigger asChild>
        <Button className="bg-blue-600 hover:bg-blue-800">New Issue</Button>
      </SheetTrigger>
      <SheetContent side="right">
        <SheetHeader>
          <SheetTitle>New Issue</SheetTitle>
        </SheetHeader>
        <div>
          <CreateIssueForm
            onSuccess={() => setOpen(false)}
            projectCode={projectCode}
          />
        </div>
      </SheetContent>
    </Sheet>
  );
}
