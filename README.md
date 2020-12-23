# kotlin-firestore-sample

## 手順メモ

①ユーザー一覧を作成する
　→ユーザーセルを作成
　→RecyclerViewで対応＆SearchViewで検索できるようにする
②プロファイル画面を作成する
　→ログインユーザーとそれ以外のユーザーで分ける
　→それ以外のユーザーはProfileActivityで遷移してきて、そこからFragmentを利用して表示する
③Tweetできるようにする
　→Tweet用のActivity作成する。
　→ボタンを押したら遷移する。
　→そこで投稿もしくはキャンセルしたら一覧画面に戻る
④Tweet詳細画面作作成＆いいね・コメントできるようにする
　


## メモ
分からない＆あとで調べるメモ
①Taskや非同期処理が良くわかってない
　→処理の仕組みと、分離の仕方



実装メモ
ログイン機能(viewModel,view,repository)

流れ
①ボタンを押す
　→入力項目が記入されていたら処理開始
　→viewで呼び出し
②viewModelで処理
　→入力された値を元にログイン処理を行う
　→repositoryで処理を任せる
③repository
　→実際に処理を行う
　→taskが完了したのをviewModel側に通知したい
　　・エラーが出た場合はそれをviewModelで反映させる

repository内でviewModel側から監視するプロパティを作成しておく
↓
その値をviewModel側からの呼び出しで変更
↓
viewModelでその変更を感知して、view側に伝える
↓
ステータスを確認して、それに応じた処理を行う。
　
