package com.dum.dodam.Login;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.R;

public class SignUP extends Fragment {
    private CheckBox checkAll;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private Button btnContinue;
    private TextView contract1;
    private TextView contract2;
    private TextView contract3;
    private TextView contract4;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup1, container, false);

        btnContinue = view.findViewById(R.id.continues);
        checkAll = view.findViewById(R.id.checkAll);
        checkBox1 = view.findViewById(R.id.check1);
        checkBox2 = view.findViewById(R.id.check2);
        checkBox3 = view.findViewById(R.id.check3);
        checkBox4 = view.findViewById(R.id.check4);
        contract1 = view.findViewById(R.id.contract1);
        contract2 = view.findViewById(R.id.contract2);
        contract3 = view.findViewById(R.id.contract3);
        contract4 = view.findViewById(R.id.contract4);

        contract1.setMovementMethod(new ScrollingMovementMethod());
        contract2.setMovementMethod(new ScrollingMovementMethod());
        contract3.setMovementMethod(new ScrollingMovementMethod());
        contract4.setMovementMethod(new ScrollingMovementMethod());

        contract1.setText("[개인정보 처리 방침 동의] (필수)\n" +
                "\n" +
                "<덤덤>('도담도담')은(는) 개인정보보호법에 따라 이용자의 개인정보 보호 및 권익을 보호하고 개인정보와 관련한 이용자의 고충을 원활하게 처리할 수 있도록 다음과 같은 처리방침을 두고 있습니다.\n" +
                "<덤덤>('도담도담') 은(는) 회사는 개인정보처리방침을 개정하는 경우 웹사이트 공지사항(또는 개별공지)을 통하여 공지할 것입니다.\n" +
                "○ 본 방침은부터 2020년 10월 7일부터 시행됩니다.\n" +
                "1. 개인정보의 처리 목적 <덤덤>(이하 '도담도담')은(는) 개인정보를 다음의 목적을 위해 처리합니다. 처리한 개인정보는 다음의 목적이외의 용도로는 사용되지 않으며 이용 목적이 변경될 시에는 사전동의를 구할 예정입니다.\n" +
                "가. 홈페이지 회원가입 및 관리\n" +
                "회원 가입의사 확인, 회원제 서비스 제공에 따른 본인 식별·인증, 회원자격 유지·관리, 제한적 본인확인제 시행에 따른 본인확인, 서비스 부정이용 방지 등을 목적으로 개인정보를 처리합니다.\n" +
                "\n" +
                "나. 재화 또는 서비스 제공\n" +
                "서비스 제공, 콘텐츠 제공, 본인인증, 연령인증 등을 목적으로 개인정보를 처리합니다.\n" +
                "\n" +
                "다. 마케팅 및 광고에의 활용\n" +
                "이벤트 및 광고성 정보 제공 및 참여기회 제공, 서비스의 유효성 확인, 접속빈도 파악 또는 회원의 서비스 이용에 대한 통계 등을 목적으로 개인정보를 처리합니다.\n" +
                "\n" +
                "2. 개인정보 파일 현황\n" +
                "●\t1. 개인정보 파일명 : 도담도담 이용자 정보\n" +
                "-학교 인증\n" +
                "개인정보 항목: 로그인ID, 성별, 생년월일, 이름, 학교\n" +
                "수집 방법: 학생증 사본\n" +
                "보유 근거: 회원가입 및 서비스 제공을 위한 본인인증\n" +
                "보유기간: 인증 후 지체없이 파기\n" +
                "관련 법령: 신용정보의 수집/처리 및 이용 등에 관한 기록: 3년\n" +
                "- 이벤트 참여\n" +
                "개인정보 항목: 휴대전화번호\n" +
                "수집방법: 직접 입력\n" +
                "보유 근거: 이벤트 상품 수령을 위한 정보 수집\n" +
                "보유 기간: 회원 탈퇴 시까지\n" +
                "관련 법령: 신용 정보의 수집/처리 및 이용 등에 관한 기록: 3년\n" +
                "*각 항목 또는 추가적으로 수집이 필요한 개인정보 및 개인정보를 포함한 자료는 이용자 응대 과정과 서비스 내부 알림 수단 등을 통해 별도로 요청, 수집될 수 있습니다.\n" +
                "*서비스 이용과정에서 이용기록, 로그 기록이 자동으로 수집될 수 있습니다.\n" +
                "3. 개인정보의 처리 및 보유 기간\n" +
                "\n" +
                "① <덤덤>('도담도담')은(는) 법령에 따른 개인정보 보유·이용기간 또는 정보주체로부터 개인정보를 수집시에 동의 받은 개인정보 보유,이용기간 내에서 개인정보를 처리,보유합니다.\n" +
                "\n" +
                "② 각각의 개인정보 처리 및 보유 기간은 다음과 같습니다.\n" +
                "●\t1.<홈페이지 회원가입 및 관리>\n" +
                "●\t<홈페이지 회원가입 및 관리>와 관련한 개인정보는 수집.이용에 관한 동의일로부터<지체없이 파기>까지 위 이용목적을 위하여 보유.이용됩니다.\n" +
                "●\t보유근거 : 회원가입 및 서비스 제공을 위한 본인인증\n" +
                "●\t관련법령 : 신용정보의 수집/처리 및 이용 등에 관한 기록 : 3년\n" +
                "4. 정보주체와 법정대리인의 권리·의무 및 그 행사방법 이용자는 개인정보 주체로서 다음과 같은 권리를 행사할 수 있습니다.\n" +
                "① 정보주체는 덤덤에 대해 언제든지 개인정보 열람, 정정, 삭제, 처리정지 요구 등의 권리를 행사할 수 있습니다.\n" +
                "② 제1항에 따른 권리 행사는덤덤에 대해 개인정보 보호법 시행령 제41조제1항에 따라 서면, 전자우편, 모사전송(FAX) 등을 통하여 하실 수 있으며 덤덤은(는) 이에 대해 지체 없이 조치하겠습니다.\n" +
                "③ 제1항에 따른 권리 행사는 정보주체의 법정대리인이나 위임을 받은 자 등 대리인을 통하여 하실 수 있습니다. 이 경우 개인정보 보호법 시행규칙 별지 제11호 서식에 따른 위임장을 제출해야 합니다.\n" +
                "④ 개인정보 열람 및 처리정지 요구는 개인정보보호법 제35조 제5항, 제37조 제2항에 의하여 정보주체의 권리가 제한 될 수 있습니다.\n" +
                "⑤ 개인정보의 정정 및 삭제 요구는 다른 법령에서 그 개인정보가 수집 대상으로 명시되어 있는 경우에는 그 삭제를 요구할 수 없습니다.\n" +
                "⑥ 덤덤은(는) 정보주체 권리에 따른 열람의 요구, 정정·삭제의 요구, 처리정지의 요구 시 열람 등 요구를 한 자가 본인이거나 정당한 대리인인지를 확인합니다.\n" +
                "5. 처리하는 개인정보의 항목 작성\n" +
                "\n" +
                "① <덤덤>('이하 '도담도담')은(는) 다음의 개인정보 항목을 처리하고 있습니다.\n" +
                "●\t1<홈페이지 회원가입 및 관리>\n" +
                "●\t필수 항목 : 로그인ID, 성별, 생년월일, 이름, 학력, 서비스 이용 기록, 접속 로그\n" +
                "●\t- 선택 항목 : 휴대전화번호\n" +
                "6. 개인정보의 파기<덤덤>('도담도담')은(는) 원칙적으로 개인정보 처리목적이 달성된 경우에는 지체없이 해당 개인정보를 파기합니다. 파기의 절차, 기한 및 방법은 다음과 같습니다.\n" +
                "-파기절차\n" +
                "이용자가 입력한 정보는 목적 달성 후 별도의 DB에 옮겨져(종이의 경우 별도의 서류) 내부 방침 및 기타 관련 법령에 따라 일정기간 저장된 후 혹은 즉시 파기됩니다. 이 때, DB로 옮겨진 개인정보는 법률에 의한 경우가 아니고서는 다른 목적으로 이용되지 않습니다.\n" +
                "\n" +
                "-파기기한\n" +
                "이용자의 개인정보는 개인정보의 보유기간이 경과된 경우에는 보유기간의 종료일로부터 5일 이내에, 개인정보의 처리 목적 달성, 해당 서비스의 폐지, 사업의 종료 등 그 개인정보가 불필요하게 되었을 때에는 개인정보의 처리가 불필요한 것으로 인정되는 날로부터 5일 이내에 그 개인정보를 파기합니다.\n" +
                "-파기방법\n" +
                "전자적 파일 형태의 정보는 기록을 재생할 수 없는 기술적 방법을 사용합니다\n" +
                "7. 개인정보 자동 수집 장치의 설치•운영 및 거부에 관한 사항\n" +
                "① 덤덤 은 개별적인 맞춤서비스를 제공하기 위해 이용정보를 저장하고 수시로 불러오는 ‘쿠기(cookie)’를 사용합니다. ② 쿠키는 웹사이트를 운영하는데 이용되는 서버(http)가 이용자의 컴퓨터 브라우저에게 보내는 소량의 정보이며 이용자들의 PC 컴퓨터내의 하드디스크에 저장되기도 합니다. 가. 쿠키의 사용 목적 : 이용자가 방문한 각 서비스와 웹 사이트들에 대한 방문 및 이용형태, 인기 검색어, 보안접속 여부, 등을 파악하여 이용자에게 최적화된 정보 제공을 위해 사용됩니다. 나. 쿠키의 설치•운영 및 거부 : 웹브라우저 상단의 도구>인터넷 옵션>개인정보 메뉴의 옵션 설정을 통해 쿠키 저장을 거부 할 수 있습니다. 다. 쿠키 저장을 거부할 경우 맞춤형 서비스 이용에 어려움이 발생할 수 있습니다.\n" +
                "8. 개인정보 보호책임자 작성\n" +
                "① 덤덤(이하 ‘도담도담’) 은 개인정보 처리에 관한 업무를 총괄해서 책임지고, 개인정보 처리와 관련한 정보주체의 불만처리 및 피해구제 등을 위하여 아래와 같이 개인정보보호 책임자를 지정하고 있습니다.\n" +
                "② 정보주체께서는 덤덤(이하 ‘도담도담’) 의 서비스를 이용하시면서 발생한 모든 개인정보 보호 관련 문의, 불만처리, 피해구제 등에 관한 사항을 개인정보 보호책임자 및 담당부서로 문의하실 수 있습니다. 덤덤은 정보주체의 문의에 대해 지체 없이 답변 및 처리해드릴 것입니다.\n" +
                "9. 개인정보 처리방침 변경\n" +
                "①이 개인정보처리방침은 시행일로부터 적용되며, 법령 및 방침에 따른 변경내용의 추가, 삭제 및 정정이 있는 경우에는 변경사항의 시행 7일 전부터 공지사항을 통하여 고지할 것입니다.\n" +
                "10. 개인정보의 안전성 확보 조치 <덤덤>('도담도담')은 개인정보보호법 제29조에 따라 다음과 같이 안전성 확보에 필요한 기술적/관리적 및 물리적 조치를 하고 있습니다.\n" +
                "1. 개인정보 취급 직원의 최소화 및 교육\n" +
                "개인정보를 취급하는 직원을 지정하고 담당자에 한정시켜 최소화 하여 개인정보를 관리하는 대책을 시행하고 있습니다.\n" +
                "\n" +
                "2. 개인정보의 암호화\n" +
                "이용자의 개인정보는 비밀번호는 암호화 되어 저장 및 관리되고 있어, 본인만이 알 수 있으며 중요한 데이터는 파일 및 전송 데이터를 암호화 하거나 파일 잠금 기능을 사용하는 등의 별도 보안기능을 사용하고 있습니다.\n" +
                "\n" +
                "3. 접속기록의 보관 및 위변조 방지\n" +
                "개인정보처리시스템에 접속한 기록을 최소 6개월 이상 보관, 관리하고 있으며, 접속 기록이 위변조 및 도난, 분실되지 않도록 보안기능 사용하고 있습니다.\n" +
                "\n" +
                "4. 개인정보에 대한 접근 제한\n" +
                "개인정보를 처리하는 데이터베이스시스템에 대한 접근권한의 부여, 변경, 말소를 통하여 개인정보에 대한 접근통제를 위하여 필요한 조치를 하고 있으며 침입차단시스템을 이용하여 외부로부터의 무단 접근을 통제하고 있습니다.\n" +
                "\n" +
                "5. 문서보안을 위한 잠금장치 사용\n" +
                "개인정보가 포함된 서류, 보조저장매체 등을 잠금 장치가 있는 안전한 장소에 보관하고 있습니다.\n" +
                "\n" +
                "6. 비인가자에 대한 출입 통제\n" +
                "개인정보를 보관하고 있는 물리적 보관 장소를 별도로 두고 이에 대해 출입통제 절차를 수립, 운영하고 있습니다.\n");
        contract2.setText("제1조(목적)\n" +
                "‘도담도담’ 서비스 이용약관은 회사가 서비스를 제공함에 있어, 회사와 이용자 간의 권리, 의무 및 책임 사항 등을 규정함을 목적으로 합니다.\n" +
                "제2조(정의)\n" +
                "1. 이 약관에서 사용하는 용어의 정의는 다음과 같습니다.\n" +
                " - “회사”란 서비스를 제공하는 주체를 말합니다.\n" +
                " - “서비스”란, 회사가 제공하는 모든 서비스 및 기능을 말합니다.\n" +
                " - “이용자”란, 이 약관에 따라 서비스를 이용하는 회원을 말합니다.\n" +
                " - “회원”이란, 서비스에 회원등록을 하고 서비스를 이용하는 자를 말합니다.\n" +
                " - “게시물”이란, 서비스에 게재된 문자, 사진, 영상, 첨부파일, 광고 등을 말합니다.\n" +
                " - “커뮤니티”란, 게시물을 게시할 수 있는 공간을 말합니다.\n" +
                " - “이용 기록”이란, 이용자가 서비스를 이용하면서 직접 생성한 시간표, Todo list등을 말합니다.\n" +
                " - “로그 기록”이란, 이용자가 서비스를 이용하면서 자동으로 생성된 IP주소, 접속 시간, 접속 빈도 등을 말합니다.\n" +
                "- “계정”이란, 이용계약을 통해 생성된 회원의 고유 아이디와 이에 수반하는 정보를 말합니다. \n" +
                "- “서비스 내부 알림 수단”이란, 팝업, 알림, 공지사항, 내 정보 메뉴 등을 말합니다.\n" +
                "- “연락처”란, 회원가입, 본인 인증, 문의 창구 등을 통해 수집된 이용자의 휴대전화 번호 등을 의미합니다. \n" +
                "- “관련법”이란, 정보통신망 이용 촉진 및 정보보호 등에 관한 법률, 개인정보보호법, 통신비밀보호법 등 관련 있는 국내 법령을 말합니다.\n" +
                "- “본인 인증”이란, 휴대전화 번호 등을 이용한 본인 확인 절차를 말합니다.\n" +
                "- “학교 인증”이란, 학생증을 이용한 학적 확인 절차를 말합니다. \n" +
                "2.1항에서 정의되지 않은 약관 내 용어의 의미는 일반적인 이용관행에 의합니다.\n" +
                "제 3조 (약관 등의 명시와 설명 및 개정)\n" +
                "1.\t회사는 이 약관을 회원가입 화면 및 “내 정보” 메뉴 등에 게시합니다.\n" +
                "2.\t회사는 관련법을 위배하지 않는 범위에서 이 약관을 개정할 수 있습니다.\n" +
                "3.\t개정 내용이 회원에게 불리할 경우, 적용일자 및 개정사유를 명시하여 현행약관과 함께 “공지사항”에 30일 간 게시합니다.\n" +
                "4.\t회원이 개정약관의 적용에 동의하지 않는 경우, 이용계약을 해지함으로써 거부의사를 표시할 수 있습니다. 단, 30일 내에 거부 의사를 표시하지 않을 경우 약관에 동의한 것으로 간주합니다.\n" +
                "5.\t회원은 약관 일부분만을 동의 또는 거부할 수 있습니다.\n" +
                "제 4조(서비스의 제공)\n" +
                "1.\t회사는 다음 서비스를 제공합니다.\n" +
                "-교내 생활 편의 서비스\n" +
                "-커뮤니티 서비스\n" +
                "-공모전, 스터디, 입시 정보 제공 서비스\n" +
                "-다른 회사 및 단체와의 협력을 통해 제공하는 서비스\n" +
                "-기타 회사가 정하는 서비스\n" +
                "2. 회사는 운영상, 기술상의 필요에 따라 제공하고 있는 서비스를 변경할 수 있습니다.\n" +
                "3. 회사는 이용자의 개인정보 및 서비스 이용 기록에 따라 서비스 이용에 차이를 둘 수 있습니다.\n" +
                "4. 회사는 천재지변, 인터넷 장애, 경영 악화, 학기 중단 등으로 인해 서비스를 더 이상 제공하기 어려울 경우, 서비스를 통보 없이 중단할 수 있습니다. \n" +
                "5. 회사는 1항부터 전항까지 다음 내용으로 인해 발생한 피해에 대해 어떠한 책임도 지지 않습니다.\n" +
                "- 모든 서비스, 게시물, 이용 기록의 진본성, 무결성, 신뢰성, 이용가능성\n" +
                "- 서비스 이용 중 타인과 상호 간에 합의한 내용\n" +
                "-게시물, 광고의 버튼, 하이퍼링크 등 외부로 연결된 서비스와 같이 회사가 제공하지 않은 서비스에서 발생한 피해\n" +
                "-이용자의 귀책 사유 또는 회사의 귀책 사유가 아닌 사유로 발생한 이용자의 피해\t\n" +
                "제 5조\n" +
                "1.\t만 15세 미만의 이용자는 서비스를 이용하거나 회원가입을 할 수 없으며, 그럼에도 불구하고 성립된 회원가입은 무효로 간주됩니다.\n" +
                "2.\t회사와 회원의 서비스 이용계약은 “카카오톡 로그인”을 통한 본인인증 및 가입양식에 따라 회원정보를 제공한 후 필수 약관에 동의한다는 의사표시를 한 비회원의 이용신청에 대하여, 서비스 화면에 이용승낙을 표시하는 방법 등으로 의사 표시를 하면서 체결됩니다.\n" +
                "3.\t승낙은 신청순서에 따라 순차적으로 처리되며, 회원가입의 성립시기는 회사의 승낙이 회원에게 도달한 시점으로 말합니다.\n" +
                "4.\t회사는 부정사용방지 및 본인확인을 위해 회원에게 본인 인증 및 학교 인증을 요청할 수 있습니다.\n" +
                "제 6조(개인정보의 관리 및 보호)\n" +
                "1.\t회원이 회사와 체결한 서비스 이용계약은 처음 이용계약을 체결한 본인에 한해 적용됩니다.\n" +
                "2.\t회원은 회원가입시 등록한 정보에 변동이 있을 경우, 즉시 “내 정보” 메뉴 등을 이용하여 정보를 최신화해야 합니다.\n" +
                "3.\t등록한 학교 정보는 변경할 수 없으며, 학교 정보가 바뀐 경우 회원이 회사에 연락을 취해야합니다. \n" +
                "회사는 회원이 1항부터 전항까지 이행하지 않아 발생한 피해에 대해 어떠한 책임도 지지 않습니다.\n" +
                "제7조(서비스 이용계약의 종료)\n" +
                "1.\t회원은 언제든지 본인의 계정으로 로그인한 뒤 서비스 내부의 “탈퇴하기”버튼을 누르는 방법으로 탈퇴를 요청할 수 있습니다. 회사는 해당 요청을 확인한 후 탈퇴를 처리합니다.\n" +
                "2.\t탈퇴 처리가 완료되었더라도, 회원이 게시한 게시물은 삭제되지 않습니다.\n" +
                "3.\t탈퇴 후 재가입은 30일 이후에 가능합니다. \n" +
                "4.\t회사는 회원이 연속하여 1년 동안 로그인을 하지 않을 경우, 회원의 동의 없이 회원자격을 박탈할 수 있습니다.\n" +
                "5.\t회사는 천재지변, 테러, 폐교, 학기 종료 등 불가피한 사유로 더 이상 서비스를 제공할 수 없을 경우, 회원의 동의없이 회원자격을 박탈할 수 있습니다.\n" +
                "회사는 회원이 1항부터 전항까지 불이행으로 인해 발생한 피해에 대해 어떠한 책임도 지지 않습니다.\n" +
                "제8조(회원에 대한 통보)\n" +
                "1.\t회사가 회원에 대한 통보를 하는 경우 서비스 내부 알림 수단을 이용합니다.\n" +
                "2.\t회사는 다수의 회원에 대한 통보를 할 경우 공지사항 등에 게시함으로써 개별통보를 대체할 수 있습니다.\n" +
                "3.\t회원이 30일 이내에 의사 표시를 하지 않을 경우 통보 내용에 대해 동의한 것으로 간주합니다.\n" +
                "제9조(저작권의 귀속)\n" +
                "1.\t회사는 유용하고 편리한 서비스를 제공하기 위해, 2020년부터 서비스 및 서비스 내부의 기능(각종 커뮤니티, 라이브 쑈, 공모전 정보 등)의 체계와 다양한 기능(시간표, ToDo list, 급식표 등)을 직접 설계 및 운영하고 있는 데이터베이스 제작자에 해당합니다. 회사는 저작권법에 따라 데이터베이스 제작자는 복제권 및 전송권을 포함한 데이터베이스 전부에 대한 권리를 가지고 있으며, 이는 법률에 따라 보호받는 대상입니다. 그러므로 이용자는 데이터 베이스 제작자인 회사의 승인 없이 데이터베이스의 전부 또는 일부를 복제, 배포 발생 또는 전송할 수 없습니다. \n" +
                "2.\t회사가 작성한 게시물에 대한 권리는 회사에게 귀속되며, 회원이 작성한 게시물의 권리는 회원에게 귀속됩니다. 단, 회사는 서비스의 운영, 확장, 홍보 등의 필요한 목적으로 회원의 저작물을 합리적이고, 필요한 범위 내에서 별도의 허락없이 수정하여 무상으로 사용하거나 제휴사에게 제공할 수 있습니다. 이 경우, 회원의 개인정보는 제공하지 않습니다.\n" +
                "3.\t회사는 전항 이외의 방법으로 회원의 게시물을 이용할 경우, 서비스 내부 알림 수단을 이용하여 회원의 동의를 받아야합니다.\n" +
                "제 10조(게시물의 게시 중단)\n" +
                "1.\t회사는 관련법에 의거하여 회원의 게시물로 인한 법률상 이익 침해를 근거로, 다른 이용자 또는 제3자가 회원 또는 회사를 대상으로 하여 민형사상의 법적 조치를 취하거나, 관련된 게시물의 게시 중단을 요청하는 경우, 회사는 해당 게시물에 대한 접근을 잠정적으로 제한하거나 삭제할 수 있습니다.\n" +
                "제 11조(광고의 게재 및 발신)\n" +
                "1.\t회사는 서비스의 제공을 위해 서비스 내부에 광고를 게재할 수 있습니다.\n" +
                "2.\t회사는 이용자의 이용 기록을 활용한 광고를 게재할 수 있습니다.\n" +
                "3.\t회사는 회원이 광고성 정보 수신에 동의할 경우, 서비스 내부 알림 수단과 회원의 연락처를 이용하여 광고성 정보를 발신할 수 있습니다.\n" +
                "4.\t회사는 광고 게재 및 동의 된 광고성 정보의 발신으로 인해 발생한 피해에 대해 어떠한 책임을 지지 않습니다.\n" +
                "제 12조(금지행위)\n" +
                "1.\t이용자는 다음과 같은 행위를 해서는 안됩니다.\n" +
                "-개인정보 또는 계정 기만, 침해, 공유 행위\n" +
                "▶ 개인정보를 허위, 누락, 오기, 도용하여 작성하는 행위\n" +
                "▶타인의 개인정보 및 계정을 수집, 저장, 공개, 이용하는 행위\n" +
                "▶자신과 타인의 개인정보를 제3자에게 공개, 양도하는 행위\n" +
                "▶다중 계정을 생성 및 이용하는 행위\n" +
                "▶자신의 계정을 이용하여 타인의 요청을 이행하는 행위\n" +
                " - 시스템 부정행위\n" +
                " ▶해당 고등학교의 재학생 및 졸업생이 아닌 이용자가 서비스를 이용하는 행위\n" +
                " ▶허가하지 않은 방식의 서비스 이용 행위 \n" +
                " ▶회사의 모든 재산에 대한 침해 행위\n" +
                "-업무 방해 행위\n" +
                " ▶서비스 관리자 또는 이에 준하는 자격을 사칭하거나 허가없이 취득하여 직권을 행사하는 행위\n" +
                " ▶회사 및 타인의 명예를 손상시키거나 업무를 방해하는 행위\n" +
                " ▶서비스 내부 정보 일체를 허가 없이 이용, 변조, 삭제 및 외부로 유출하는 행위\n" +
                " ▶이 약관, 개인정보 처리방침, 커뮤니티 이용 규칙에서 이행 및 비이행을 명시한 내용에 반하는 행위\n" +
                "▶기타 현행법에 어긋나거나 부적절하다고 판단되는 행위\n" +
                "-이용자가 1항에 해당하는 행위를 할 경우, 회사는 다음과 같은 조치들을 영구적으로 취할 수 있습니다.\n" +
                "▶이용자의 서비스 이용 권한, 자격, 혜택 제한 및 회수\n" +
                "▶회원과 체결된 이용계약을 회원의 동의나 통보 없이 파기\n" +
                "▶회원가입, 본인 인증 거부\n" +
                "▶회원의 커뮤니티, 게시물, 이용기록을 임의로 삭제, 중단, 변경\n" +
                "▶그 외 회사가 필요하다고 판단되는 조치\n" +
                "2.\t회사는 1항부터 전항까지로 인해 발생한 피해에 대해 어떠한 책임도 지지 않으며, 이용하는 귀책사유로 인해 발생한 모든 손해를 배상할 책임이 있습니다.\n" +
                "제 13조(재판권 및 준거법)\n" +
                "1.\t회사와 이용자 간에 발생한 분쟁에 관한 소송은 대한민국 서울중앙지방법원을 관할 법원으로 합니다. 다만, 제소 당시 이용자의 주소 또는 거소가 분명하지 않거나 외국 거주자의 경우에는 민사소송법상의 관할법원에 제기합니다. \n" +
                "2.\t회사와 이용자 간에 제기된 소송에는 한국법을 적용합니다.\n" +
                "제 14조(기타)\n" +
                "1.\t이 약관은 2020년 10월 7일에 최신화 되었습니다.\n" +
                "2.\t이 약관에서 정하지 않은 사항과 이 약관의 해석에 관해 관련법 또는 관례에 따릅니다. \n" +
                "3.\t이 약관에도 불구하고 다른 약관이나 서비스 이용 중 안내 문구 등으로 달리 정함이 있는 경우에는 해당 내용을 우선으로 합니다.\n");
        contract3.setText("[본인 명의 가입 확인](필수)\n" +
                "부모님, 친구 등 타인의 명의로 가입할 수 없습니다. ‘도담도담’은 철저한 학교 인증과 안전한 익명 커뮤니티를 제공하기 위해 가입시 본인 인증 수단을 이용하여 본인 여부를 확인하고, 커뮤니티 이용 시 증명 자료 제출을 통해 재학 및 졸업 여부를 확인합니다. 두 정보가 일치하지 않을 경우 서비스를 이용하실 수 없습니다. \n");
        contract4.setText("위와 같이 동의를 받아 수집한 귀하의 개인정보는 보유∙이용기간 중에 전화, SMS, LMS, MMS, 이메일, 우편을 통하여 뉴스레터, 제품정보, 경연대회, 판촉, 설문조사, 사이트 기능 관리, 안부 문자, 행사 안내 등 영리목적의 광고성 정보 전달에 활용되거나 영업 및 마케팅 목적으로 활용될 수 있습니다. 귀하는 위와 같은 마케팅 목적 개인정보 수집 및 이용을 거부할 수 있으나, 이 경우 행사 안내를 받지 못하거나 도담도담이 제공하는 유용한 정보를 받지 못할 수 있습니다.");

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll.isChecked()) {
                    if (!checkBox1.isChecked()) checkBox1.toggle();
                    if (!checkBox2.isChecked()) checkBox2.toggle();
                    if (!checkBox3.isChecked()) checkBox3.toggle();
                    if (!checkBox4.isChecked()) checkBox4.toggle();
                } else {
                    if (checkBox1.isChecked()) checkBox1.toggle();
                    if (checkBox2.isChecked()) checkBox2.toggle();
                    if (checkBox3.isChecked()) checkBox3.toggle();
                    if (checkBox4.isChecked()) checkBox4.toggle();
                }
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked() && checkBox4.isChecked()) {
                    if (!checkAll.isChecked()) checkAll.toggle();
                } else {
                    if (checkAll.isChecked()) checkAll.toggle();
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked()){
                    ((startUpActivity) getActivity()).replaceFragment(new SIgnUP2());
                }
                else {
                    Toast.makeText(getContext(), "필수 약관에 동의해야지만 가입할 수 있습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}
