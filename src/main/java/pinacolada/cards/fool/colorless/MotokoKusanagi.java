package pinacolada.cards.fool.colorless;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import pinacolada.cards.base.CardSeries;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLAttackType;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.fool.FoolCard;
import pinacolada.effects.AttackEffects;
import pinacolada.utilities.PCLActions;

public class MotokoKusanagi extends FoolCard
{
    public static final int GOLD_THRESHOLD = 150;
    public static final int BASE_RICOCHET = 4;

    public static final PCLCardData DATA = Register(MotokoKusanagi.class).SetAttack(1, CardRarity.UNCOMMON, PCLAttackType.Ranged).SetColor(CardColor.COLORLESS).SetSeries(CardSeries.GhostInTheShell);

    public MotokoKusanagi()
    {
        super(DATA);

        Initialize(3, 0, 2);
        SetUpgrade(2, 0, 0);
        SetAffinity_Blue(1, 0, 0);
        SetAffinity_Silver(1, 0, 0);
        SetAffinity_Green(1, 0, 1);

        SetRicochet(BASE_RICOCHET, 0, this::OnCooldownCompleted);

        SetExhaust(true);
        SetHitCount(2);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        this.baseCooldownValue = this.cooldownValue = Math.max(1, BASE_RICOCHET - Math.floorDiv(player.gold, GOLD_THRESHOLD));
    }

    @Override
    public void triggerOnExhaust()
    {
        super.triggerOnExhaust();
        PCLActions.Bottom.GainBlur(magicNumber);
        PCLActions.Bottom.GainSupportDamage(magicNumber);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.DealCardDamage(this, m, AttackEffects.GUNSHOT);
    }

    protected void OnCooldownCompleted(AbstractMonster m)
    {
        PCLActions.Bottom.MoveCard(this, player.exhaustPile, player.drawPile)
                .ShowEffect(true, false);
    }
}