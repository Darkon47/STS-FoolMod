package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.cards.AbstractCard;
import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.interfaces.subscribers.OnSynergySubscriber;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.base.PCLCardTarget;
import pinacolada.cards.base.attributes.AbstractAttribute;
import pinacolada.cards.base.attributes.TempHPAttribute;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.PCLCombatStats;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;

public class WishesUnnumbered extends EternalCard implements OnSynergySubscriber
{
    public static final PCLCardData DATA = Register(WishesUnnumbered.class).SetSkill(2, CardRarity.RARE, PCLCardTarget.Self);

    public WishesUnnumbered()
    {
        super(DATA);

        Initialize(0, 1, 7, 2);
        SetUpgrade(0, 1, 0, 1);

        SetLight();
        SetExhaust(true);
    }

    @Override
    protected void OnUpgrade() {
        super.OnUpgrade();
        SetHaste(true);
    }

    @Override
    public AbstractAttribute GetSpecialInfo()
    {
        return TempHPAttribute.Instance.SetCard(this, true);
    }

    @Override
    public void triggerWhenCreated(boolean startOfBattle)
    {
        super.triggerWhenCreated(startOfBattle);
        PCLCombatStats.onSynergy.Subscribe(this);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.GainBlock(block);
        PCLActions.Bottom.GainTemporaryHP(magicNumber);
    }

    @Override
    public void OnSynergy(AbstractCard card) {
        if (player.hand.contains(this)) {
            PCLGameUtilities.IncreaseMagicNumber(this, secondaryValue, false);
        }
    }
}