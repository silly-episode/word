<!--
 * @Author: dengyinzhe
 * @Date: 2023/5/7 20:29
 * @FileName: MyEditor3
 * @LastEditors: 2023/5/7 20:29
 * @LastEditTime: 2023/5/7 20:29
 * @Description:
-->
<template>
  <div>
    <div style="border: 1px solid #ccc">
      <!-- 工具栏 -->
      <Toolbar
          :defaultConfig="toolbarConfig"
          :editor="editor"
          style="border-bottom: 1px solid #ccc"
      />
      <!-- 编辑器 -->
      <Editor
          v-model="html"
          :defaultConfig="editorConfig"
          :editor="editor"
          style="height: 520px"
          @onChange="onChange"
          @onCreated="onCreated"
      />
    </div>
  </div>
</template>

<script>
import {Boot} from "@wangeditor/editor";
import {Editor, Toolbar} from "@wangeditor/editor-for-vue";
import formulaModule from "@wangeditor/plugin-formula";
// console.log('formulaModule', formulaModule)
Boot.registerModule(formulaModule);

export default {
  name: "MyEditor3",
  components: {Editor, Toolbar},
  props: {
    content: {
      type: String,
      default: '',
    },
  },
  data() {
    return {
      editor: null,
      html: "",
      toolbarConfig: {
        // toolbarKeys: [ /* 显示哪些菜单，如何排序、分组 */ ],
        // excludeKeys: [ /* 隐藏哪些菜单 */ ],
        insertKeys: {
          index: 0,
          keys: [
            "insertFormula", // “插入公式”菜单
            // 'editFormula' // “编辑公式”菜单
          ],
        },
      },
      editorConfig: {
        placeholder: "请输入内容...",
        autoFocus: true,

        // 所有的菜单配置，都要在 MENU_CONF 属性下
        MENU_CONF: {},

        hoverbarKeys: {
          formula: {
            menuKeys: ["editFormula"], // “编辑公式”菜单
          },
        },
      },
    };
  },

  created() {
    this.html = this.content;
    console.log('this.html', this.html)
  },
  methods: {
    onCreated(editor) {
      this.editor = Object.seal(editor); // 【注意】一定要用 Object.seal() 否则会报错
    },
    onChange(editor) {
      // editor.getHtml() 等于 this.html
      this.$emit('changeData', this.html)
    },
    getEditorText() {
      const editor = this.editor;
      if (editor == null) return;
      console.log("1111  " + editor.getText()); // 执行 editor API
    },

    clearContent() {
      const editor = this.editor;
      if (editor == null) return;
      editor.clear();
    },

  },
  beforeDestroy() {
    console.log("dasjfkasjdf")
    this.html = ""
    const editor = this.editor;
    if (editor == null) return;
    editor.destroy(); // 组件销毁时，及时销毁 editor ，重要！！！
  },
};
</script>

<style src="@wangeditor/editor/dist/css/style.css"></style>

