package pinacolada.cards.fool.series.GenshinImpact;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import com.megacrit.cardcrawl.powers.watcher.VigorPower;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class Bennett extends FoolCard {
    public static final PCLCardData DATA = Register(Bennett.class).SetAttack(1, CardRarity.COMMON, PCLAttackType.Normal).SetSeriesFromClassPackage();
    public static final int SELF_DAMAGE = 4;

    public Bennett() {
        super(DATA);

        Initialize(10, 0, 5, 2);
        SetUpgrade(4, 0, 0);
        SetAffinity_Red(1, 0 ,1);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info) {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.BLUNT_HEAVY);
        PCLActions.Bottom.StackPower(new VigorPower(player, magicNumber));
        if (info.IsSynergizing) {
            PCLActions.Bottom.StackPower(new VigorPower(player, secondaryValue));
        }
        PCLActions.Bottom.DealDamageAtEndOfTurn(player, player, SELF_DAMAGE, AttackEffects.BLUNT_HEAVY);
    }
}