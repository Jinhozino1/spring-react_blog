// 🔹 declarations.d.ts (또는 src/types/images.d.ts)
declare module '*.png' {
    const value: string;
    export default value;
  }