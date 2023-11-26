export function Spinner({ text = "Loading..." }: { text: string }) {
  return (
    <div className="flex flex-col gap-y-2 items-center">
      <div
        className="inline-block h-12 w-12 text-gray-800 animate-spin rounded-full border-2 border-solid border-current border-r-transparent align-[-0.125em] motion-reduce:animate-[spin_1.5s_linear_infinite]"
        role="status"
      ></div>
      <span className="tracking-wider">{text}</span>
    </div>
  );
}
