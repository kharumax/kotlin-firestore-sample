# kotlin-firestore-sample

## 手順メモ
次やること
④Tweet出来るようにする
　→Home FragmentからTweetFragmentに移動して、FeedViewModelからユーザー情報を取得する
　→FeedViewModelで新規投稿を行う＋投稿データを新しくする関数を呼び出し
　→HomeFragmentに戻る
　

## 後でやるメモ
①通信中にインジゲーターを表示する
　→プロフィール更新中
　→



## 実装メモ


②
プロフィール
・プロフィール写真
・ユーザー名
・フルネーム
・ボタン（）
・投稿(ユーザーが投稿したもの、いいねしたもの）
　→ここは一旦置いておく

・ProfileViewModelはログイン中のユーザーのみ利用
・他のユーザーはActivity経由でプロフィールページを表示する
　→ユーザー情報を更新できるようにする


①ユーザー一覧を作成する
　→ユーザーセルを作成
　→RecyclerViewで対応＆SearchViewで検索できるようにする
②プロファイル画面を作成する
　→自分のユーザーの情報を表示する
　→ProfileFragmentからsafe argsでviewModelを渡して、その場で遷移できるようにする
③他のユーザーのプロフィール画面を作成＆フォローできるようにする
　→SearchFragmentからsafe argsでUserを渡して遷移する
④Tweetできるようにする
　→Tweet用のActivity作成する。
　→ボタンを押したら遷移する。
　→そこで投稿もしくはキャンセルしたら一覧画面に戻る
⑤Tweet詳細画面作作成＆いいね・コメントできるようにする
　


## メモ
分からない＆あとで調べるメモ
①Taskや非同期処理が良くわかってない
　→処理の仕組みと、分離の仕方





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
　
