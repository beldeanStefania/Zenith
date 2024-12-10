// utils.ts
export function generateBoxShadows(count: number): string {
    let shadows = '';
    for (let i = 0; i < count; i++) {
      const x = Math.floor(Math.random() * 2000);
      const y = Math.floor(Math.random() * 2000);
      shadows += `${x}px ${y}px #FFF${i < count - 1 ? ', ' : ''}`;
    }
    return shadows;
  }
  