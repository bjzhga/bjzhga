import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import Components from 'unplugin-vue-components/vite'
import Icons from 'unplugin-icons/vite'
import IconsResolver from 'unplugin-icons/resolver'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    // 下面是新增的配置
    Components({
      resolvers: [
        // 自动解析图标，前缀是 i
        IconsResolver({
          prefix: 'i',
        }),
      ],
    }),
    Icons({
      autoInstall: true, // 自动安装图标集
    }),
  ],
})