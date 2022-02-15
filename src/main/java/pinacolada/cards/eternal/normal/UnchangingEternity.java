package pinacolada.cards.eternal.normal;

import com.megacrit.cardcrawl.characters.AbstractPlayer;
import com.megacrit.cardcrawl.core.AbstractCreature;
import com.megacrit.cardcrawl.monsters.AbstractMonster;
import eatyourbeets.utilities.TargetHelper;
import pinacolada.cards.base.CardUseInfo;
import pinacolada.cards.base.PCLCardData;
import pinacolada.cards.eternal.EternalCard;
import pinacolada.powers.EternalPower;
import pinacolada.utilities.PCLActions;
import pinacolada.utilities.PCLGameUtilities;
import pinacolada.utilities.PCLJUtils;

public class UnchangingEternity extends EternalCard
{
    public static final PCLCardData DATA = Register(UnchangingEternity.class)
            .SetMaxCopies(2)
            .SetPower(3, CardRarity.RARE);

    public UnchangingEternity()
    {
        super(DATA);

        Initialize(0, 0, 1, 0);
        SetUpgrade(0, 0, 1);

        SetLight();
        SetDelayed(true);
    }

    @Override
    public void OnUse(AbstractPlayer p, AbstractMonster m, CardUseInfo info)
    {
        PCLActions.Bottom.StackPower(new UnchangingEternityPower(player, magicNumber));
    }

    public static class UnchangingEternityPower extends EternalPower
    {
        public int cache;

        public UnchangingEternityPower(AbstractCreature owner, int amount)
        {
            super(owner, UnchangingEternity.DATA);

            Initialize(amount);
        }

        @Override
        public void updateDescription()
        {
            this.description = FormatDescription(0, amount);
        }

        @Override
        public void atEndOfTurn(boolean isPlayer)
        {
            super.atEndOfTurn(isPlayer);
            int a = amount * PCLJUtils.Count(player.hand.group, PCLGameUtilities::HasLightAffinity);
            if (a > 0) {
                PCLActions.Bottom.ApplyShackles(TargetHelper.Enemies(), a).IgnoreArtifact(true);
            }
        }
    }
}