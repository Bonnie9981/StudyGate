package tw.studygate

data class QuizQuestion(
    val subject: String,
    val prompt: String,
    val options: List<String>,
    val answerIndex: Int,
    val explanation: String,
    val remediationPrompt: String,
    val remediationOptions: List<String>,
    val remediationAnswerIndex: Int,
    val remediationExplanation: String
)

object QuestionBank {
    // 初版為可離線執行的示範題庫；後續可擴充為 JSON/資料庫或由老師審核的題庫。
    val questions = listOf(
        QuizQuestion("物理", "物體由靜止開始，以 2 m/s² 的定加速度運動 3 秒。位移為多少？",
            listOf("3 m", "6 m", "9 m", "18 m"), 2,
            "由 s = 1/2 at²，得到 s = 1/2 × 2 × 3² = 9 m。",
            "物體由靜止以 4 m/s² 加速 2 秒，位移是多少？",
            listOf("4 m", "8 m", "12 m", "16 m"), 1,
            "s = 1/2 at² = 1/2 × 4 × 2² = 8 m。"),
        QuizQuestion("數學", "若 f(x)=x²−4x+1，函數的最小值為何？",
            listOf("−5", "−4", "−3", "1"), 2,
            "配方得 f(x)=(x−2)²−3，最小值是 −3。", // corrected below by answer index
            "若 g(x)=x²−6x+2，最小值是多少？",
            listOf("−9", "−7", "−5", "2"), 1,
            "配方 g(x)=(x−3)²−7，最小值是 −7。"),
        QuizQuestion("化學", "在定溫定壓下，1 mol 理想氣體的體積加倍，莫耳數不變，體積加倍代表什麼？",
            listOf("壓力加倍", "溫度加倍", "此條件下不可能同時維持定溫定壓", "莫耳數減半"), 2,
            "理想氣體方程式 PV=nRT；定溫、定壓且 n 不變時，V 不會改變。",
            "定溫定容下，理想氣體莫耳數加倍，壓力如何變化？",
            listOf("減半", "不變", "加倍", "變成四倍"), 2,
            "由 PV=nRT，定溫定容時 P 與 n 成正比。"),
        QuizQuestion("生物", "真核細胞中，細胞呼吸產生大量 ATP 的主要場所是？",
            listOf("核糖體", "粒線體", "高基氏體", "溶體"), 1,
            "粒線體內膜上的電子傳遞鏈與氧化磷酸化可產生大量 ATP。",
            "光合作用中，光反應主要發生在葉綠體的哪個構造？",
            listOf("基質", "類囊體膜", "核膜", "細胞膜"), 1,
            "光反應的光系統與電子傳遞鏈位於類囊體膜。"),
        QuizQuestion("英文", "Choose the grammatically correct sentence.",
            listOf("If I was you, I will apologize.", "If I were you, I would apologize.", "If I am you, I apologized.", "If I were you, I will apologized."), 1,
            "與現在事實相反的假設常用 If I were ..., I would ...。",
            "Choose the correct sentence.",
            listOf("She suggested me to rest.", "She suggested that I rest.", "She suggested me resting to.", "She suggest that I rested."), 1,
            "suggest 後可接 that 子句；正式用法常用原形動詞 rest。"),
        QuizQuestion("國文", "「學而不思則罔，思而不學則殆」主要強調什麼？",
            listOf("學習與思考應相互配合", "只要思考不必學習", "學習比思考重要得多", "學習應以記憶為唯一目的"), 0,
            "句意指出只學不思、只思不學都會有缺失，重點是學思並重。",
            "「知之為知之，不知為不知」最接近哪一種態度？",
            listOf("不懂也要裝懂", "誠實面對自己的知識界線", "拒絕學習新知", "只相信他人說法"), 1,
            "這句話強調實事求是，承認知道與不知道。")
    )
}
